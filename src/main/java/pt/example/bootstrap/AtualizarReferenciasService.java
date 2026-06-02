package pt.example.bootstrap;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pt.example.dao.RP_CONF_FAMILIA_COMPDao;
import pt.example.entity.RP_CONF_FAMILIA_COMP;

/**
 * Serviço que sincroniza as referências de uma OF com o ERP:
 * adiciona novas refs PF/COMP e remove as que já não existem.
 * Chamado por SIIP.java em POST /siip/atualizarReferencias/{id_of_cab}
 */
@Stateless
public class AtualizarReferenciasService {

    @PersistenceContext(unitName = "persistenceUnit")
    private EntityManager em;

    @Inject
    private RP_CONF_FAMILIA_COMPDao familiaCompDao;

    @SuppressWarnings("unchecked")
    public Map<String, List<String>> executar(Integer idOfCab) throws SQLException {

        // URL do ERP a partir dos parâmetros da aplicação
        String erpUrl = getURL();

        // 1. Dados do RP_OF_CAB principal
        List<Object[]> cabData = em.createNativeQuery(
            "SELECT OF_NUM, ESTADO, SEC_NUM, SEC_DES, ID_UTZ_CRIA, NOME_UTZ_CRIA, OP_COD " +
            "FROM RP_OF_CAB WHERE ID_OF_CAB = " + idOfCab
        ).getResultList();

        if (cabData.isEmpty()) throw new RuntimeException("RP_OF_CAB nao encontrado: " + idOfCab);

        String ofNum     = str(cabData.get(0)[0]);
        String estado    = str(cabData.get(0)[1]);
        String secNum    = str(cabData.get(0)[2]);
        String secDes    = str(cabData.get(0)[3]);
        String idUtzCria = str(cabData.get(0)[4]);
        String nomeUtz   = str(cabData.get(0)[5]);
        String opCod     = str(cabData.get(0)[6]);

        // 2. ofanumenr do ERP
        ConnectProgress erp = new ConnectProgress();
        List<HashMap<String, String>> ofData = erp.getOF(ofNum, erpUrl);
        if (ofData.isEmpty()) throw new RuntimeException("OF nao encontrada no ERP: " + ofNum);
        String ofanumenr = ofData.get(0).get("ofanumenr");

        // 3. Refs PF do ERP
        List<HashMap<String, String>> erpPFRefs = erp.getRef(ofanumenr, erpUrl);

        // 4. Candidatos COMP do ERP via SP_Silver_SOFC_Componentes
        List<HashMap<String, String>> erpFilhos = erp.getSPComponentes(ofanumenr, erpUrl);

        // 5. Validar famílias COMP (mesma lógica do veirificafam no frontend)
        Set<String> validCOMPs = new HashSet<>();
        Map<String, HashMap<String, String>> filhosByRef = new LinkedHashMap<>();
        for (HashMap<String, String> filho : erpFilhos) {
            String proref = filho.get("PROREF");
            String codfam = filho.get("PRDFAMCOD");
            String protyp = filho.get("PROTYPCOD");
            filhosByRef.put(proref, filho);
            if (codfam != null && !codfam.isEmpty()) {
                List<RP_CONF_FAMILIA_COMP> fams = familiaCompDao.getbycodfam(codfam);
                if (!fams.isEmpty()) validCOMPs.add(proref);
            } else if ("COM".equals(protyp) || "COMS".equals(protyp)) {
                validCOMPs.add(proref);
            }
        }

        // 6. Estado actual na BD
        // id_op_cab do OF principal (sem filho) — coluna única → List<Object>
        List<Object> opCabResult = em.createNativeQuery(
            "SELECT b.ID_OP_CAB FROM RP_OF_CAB a " +
            "INNER JOIN RP_OF_OP_CAB b ON a.ID_OF_CAB = b.ID_OF_CAB " +
            "WHERE a.ID_OF_CAB = " + idOfCab
        ).getResultList();
        if (opCabResult.isEmpty()) throw new RuntimeException("RP_OF_OP_CAB nao encontrado: " + idOfCab);
        Integer idOpCab = ((Number) opCabResult.get(0)).intValue();

        // Refs PF actuais → ref_num → id_op_lin
        List<Object[]> curPF = em.createNativeQuery(
            "SELECT ID_OP_LIN, REF_NUM, QUANT_OF FROM RP_OF_OP_LIN WHERE ID_OP_CAB = " + idOpCab
        ).getResultList();
        Map<String, Integer> currentPFMap = new LinkedHashMap<>();
        Integer quantOf = 0;
        for (Object[] row : curPF) {
            currentPFMap.put(str(row[1]), num(row[0]));
            if (quantOf == 0 && row[2] != null) quantOf = num(row[2]);
        }

        // Refs COMP actuais (OFs filhas) → ref_num → id_of_cab filho
        List<Object[]> curCOMP = em.createNativeQuery(
            "SELECT c.ID_OF_CAB, e.REF_NUM " +
            "FROM RP_OF_CAB c " +
            "INNER JOIN RP_OF_OP_CAB d ON c.ID_OF_CAB = d.ID_OF_CAB " +
            "INNER JOIN RP_OF_OP_LIN e ON d.ID_OP_CAB = e.ID_OP_CAB " +
            "WHERE c.ID_OF_CAB_ORIGEM = " + idOfCab
        ).getResultList();
        Map<String, Integer> currentCOMPMap = new LinkedHashMap<>();
        for (Object[] row : curCOMP) currentCOMPMap.put(str(row[1]), num(row[0]));

        // 7. Diferenças PF
        Set<String> newPFSet = new HashSet<>();
        for (HashMap<String, String> pf : erpPFRefs) newPFSet.add(pf.get("PROREF"));

        List<HashMap<String, String>> toAddPF = new ArrayList<>();
        for (HashMap<String, String> pf : erpPFRefs) {
            if (!currentPFMap.containsKey(pf.get("PROREF"))) toAddPF.add(pf);
        }
        List<String> toRemovePF = new ArrayList<>();
        for (String ref : currentPFMap.keySet()) {
            if (!newPFSet.contains(ref)) toRemovePF.add(ref);
        }

        // Diferenças COMP
        List<HashMap<String, String>> toAddCOMP = new ArrayList<>();
        for (String ref : validCOMPs) {
            if (!currentCOMPMap.containsKey(ref) && filhosByRef.containsKey(ref)) {
                toAddCOMP.add(filhosByRef.get(ref));
            }
        }
        List<String> toRemoveCOMP = new ArrayList<>();
        for (String ref : currentCOMPMap.keySet()) {
            if (!validCOMPs.contains(ref)) toRemoveCOMP.add(ref);
        }

        // 8. Remover refs PF
        List<String> removedPF = new ArrayList<>();
        for (String refNum : toRemovePF) {
            Integer idOpLin = currentPFMap.get(refNum);
            deleteOpLinDependencias(idOpLin);
            em.createNativeQuery("DELETE FROM RP_OF_OP_LIN WHERE ID_OP_LIN = " + idOpLin).executeUpdate();
            removedPF.add(refNum);
        }

        // Famílias de defeitos para a operação (usado ao criar novas refs PF)
        List<String> famOp = new ArrayList<>();
        if (!opCod.isEmpty()) {
            // coluna única → List<Object>
            List<Object> confOp = em.createNativeQuery(
                "SELECT ID_OP_SEC FROM RP_CONF_OP WHERE ID_OP_PRINC = '" + esc(opCod) + "'"
            ).getResultList();
            for (Object row : confOp) famOp.add(str(row).trim());
        }

        // 9. Adicionar refs PF
        List<String> addedPF = new ArrayList<>();
        for (HashMap<String, String> ref : toAddPF) {
            double perc = parseDouble(ref.get("ZPAVAL"));
            int nclqte = parseInt(ref.get("NCLQTE"), 1);

            // RP_OF_OP_LIN — obtém o ID gerado para criar dependências
            Query qLin = em.createNativeQuery(
                "INSERT INTO RP_OF_OP_LIN " +
                "(ID_OP_CAB,REF_NUM,REF_DES,REF_IND,REF_VAR1,REF_VAR2,REF_INDNUMENR," +
                "QUANT_OF,QUANT_BOAS_TOTAL,QUANT_DEF_TOTAL,PERC_OBJETIV," +
                "GESCOD,PROQTEFMT,TIPO_PECA,NCLQTE,VERSAO_MODIF) VALUES (" +
                idOpCab + ",'" + esc(ref.get("PROREF")) + "','" +
                esc(trim(ref.get("PRODES1")) + " " + trim(ref.get("PRODES2"))) + "'," +
                sqlStr(ref.get("INDREF")) + "," + sqlStr(ref.get("VA1REF")) + "," +
                sqlStr(ref.get("VA2REF")) + "," + sqlNum(ref.get("INDNUMENR")) + "," +
                (quantOf * nclqte) + ",0,0," + perc + "," +
                sqlStr(ref.get("GESCOD")) + "," + boolInt(ref.get("PROQTEFMT")) + ",'" +
                esc(ref.get("PROTYPCOD")) + "'," + nclqte + ",0); SELECT @@IDENTITY"
            );
            Integer idNewOpLin = ((BigDecimal) qLin.getSingleResult()).intValue();

            // RP_OF_OUTRODEF_LIN × 4 (id_DEF_OUTRO 1-4)
            for (int i = 1; i <= 4; i++) {
                em.createNativeQuery(
                    "INSERT INTO RP_OF_OUTRODEF_LIN (ID_OP_LIN,ID_UTZ_CRIA,ID_DEF_OUTRO) VALUES (" +
                    idNewOpLin + ",'" + esc(idUtzCria) + "'," + i + ")"
                ).executeUpdate();
            }

            // RP_OF_LST_DEF — defeitos do ERP por família da operação
            for (String fam : famOp) {
                List<HashMap<String, String>> defeitos = erp.getDefeitos(fam, erpUrl);
                for (HashMap<String, String> def : defeitos) {
                    String quacod = def.get("QUACOD");
                    // filtro igual ao frontend: substring(2,4) != '00'
                    if (quacod != null && quacod.length() >= 4 && !"00".equals(quacod.substring(2, 4))) {
                        em.createNativeQuery(
                            "INSERT INTO RP_OF_LST_DEF (ID_OP_LIN,COD_DEF,DESC_DEF,DATA_HORA_MODIF,ID_UTZ_CRIA) VALUES (" +
                            idNewOpLin + ",'" + esc(quacod) + "','" +
                            esc(def.get("QUALIB")) + "',GETDATE(),'" + esc(idUtzCria) + "')"
                        ).executeUpdate();
                    }
                }
            }

            addedPF.add(ref.get("PROREF"));
        }

        // 10. Remover COMPs (apagar OF filho completo)
        List<String> removedCOMP = new ArrayList<>();
        for (String refNum : toRemoveCOMP) {
            Integer idFilho = currentCOMPMap.get(refNum);
            String subq = "(SELECT ID_OP_LIN FROM RP_OF_OP_LIN WHERE ID_OP_CAB IN " +
                          "(SELECT ID_OP_CAB FROM RP_OF_OP_CAB WHERE ID_OF_CAB = " + idFilho + "))";
            em.createNativeQuery("DELETE FROM RP_OF_DEF_LIN WHERE ID_OP_LIN IN " + subq).executeUpdate();
            em.createNativeQuery("DELETE FROM RP_OF_OUTRODEF_LIN WHERE ID_OP_LIN IN " + subq).executeUpdate();
            em.createNativeQuery("DELETE FROM RP_OF_LST_DEF WHERE ID_OP_LIN IN " + subq).executeUpdate();
            em.createNativeQuery("DELETE FROM RP_OF_OP_ETIQUETA WHERE ID_OP_LIN IN " + subq).executeUpdate();
            em.createNativeQuery("DELETE FROM RP_OF_OP_LIN WHERE ID_OP_CAB IN " +
                "(SELECT ID_OP_CAB FROM RP_OF_OP_CAB WHERE ID_OF_CAB = " + idFilho + ")").executeUpdate();
            em.createNativeQuery("DELETE FROM RP_OF_OP_CAB WHERE ID_OF_CAB = " + idFilho).executeUpdate();
            em.createNativeQuery("DELETE FROM RP_OF_CAB WHERE ID_OF_CAB = " + idFilho).executeUpdate();
            removedCOMP.add(refNum);
        }

        // 11. Adicionar COMPs (criar OF filho)
        List<String> addedCOMP = new ArrayList<>();
        for (HashMap<String, String> filho : toAddCOMP) {
            double perc = parseDouble(filho.get("ZPAVAL"));
            int nclqte = parseInt(filho.get("NCLQTE"), 1);

            // RP_OF_CAB filho
            Query qCab = em.createNativeQuery(
                "INSERT INTO RP_OF_CAB " +
                "(ID_OF_CAB_ORIGEM,OF_NUM,OP_COD,OP_NUM,SEC_NUM,SEC_DES,MAQ_NUM,MAQ_DES," +
                "ID_UTZ_CRIA,NOME_UTZ_CRIA,DATA_HORA_CRIA,ESTADO,OP_COD_ORIGEM,OP_PREVISTA,MAQ_NUM_ORIG,VERSAO_MODIF) " +
                "VALUES (" + idOfCab + ",NULL,'60','','" + esc(secNum) + "','" + esc(secDes) + "'," +
                "'000','MAO DE OBRA','" + esc(idUtzCria) + "','" + esc(nomeUtz) + "',GETDATE()," +
                "'" + esc(estado) + "','60','2','000',0); SELECT @@IDENTITY"
            );
            Integer idNewOfCab = ((BigDecimal) qCab.getSingleResult()).intValue();

            // RP_OF_OP_CAB
            Query qOpCab = em.createNativeQuery(
                "INSERT INTO RP_OF_OP_CAB (ID_OF_CAB) VALUES (" + idNewOfCab + "); SELECT @@IDENTITY"
            );
            Integer idNewOpCab = ((BigDecimal) qOpCab.getSingleResult()).intValue();

            // RP_OF_OP_LIN
            em.createNativeQuery(
                "INSERT INTO RP_OF_OP_LIN " +
                "(ID_OP_CAB,REF_NUM,REF_DES,QUANT_OF,QUANT_BOAS_TOTAL,QUANT_DEF_TOTAL," +
                "PERC_OBJETIV,GESCOD,PROQTEFMT,TIPO_PECA,NCLQTE,VERSAO_MODIF) VALUES (" +
                idNewOpCab + ",'" + esc(filho.get("PROREF")) + "','" +
                esc(trim(filho.get("PRODES1")) + " " + trim(filho.get("PRODES2"))) + "'," +
                (quantOf * nclqte) + ",0,0," + perc + "," +
                sqlStr(filho.get("GESCOD")) + "," + boolInt(filho.get("PROQTEFMT")) + ",'" +
                esc(filho.get("PROTYPCOD")) + "'," + nclqte + ",0)"
            ).executeUpdate();

            addedCOMP.add(filho.get("PROREF"));
        }

        Map<String, List<String>> result = new LinkedHashMap<>();
        result.put("addedPF",    addedPF);
        result.put("removedPF",  removedPF);
        result.put("addedCOMP",  addedCOMP);
        result.put("removedCOMP", removedCOMP);
        return result;
    }

    // Apaga registos dependentes de um RP_OF_OP_LIN antes de o remover
    private void deleteOpLinDependencias(Integer idOpLin) {
        em.createNativeQuery("DELETE FROM RP_OF_DEF_LIN WHERE ID_OP_LIN = " + idOpLin).executeUpdate();
        em.createNativeQuery("DELETE FROM RP_OF_OUTRODEF_LIN WHERE ID_OP_LIN = " + idOpLin).executeUpdate();
        em.createNativeQuery("DELETE FROM RP_OF_LST_DEF WHERE ID_OP_LIN = " + idOpLin).executeUpdate();
        em.createNativeQuery("DELETE FROM RP_OF_OP_ETIQUETA WHERE ID_OP_LIN = " + idOpLin).executeUpdate();
    }

    @SuppressWarnings("unchecked")
    private String getURL() {
        List<Object[]> dados = em.createNativeQuery("select top 1 * from GER_PARAMETROS a").getResultList();
        return dados.isEmpty() ? "" : dados.get(0)[2].toString();
    }

    // Helpers
    private String str(Object v) { return v != null ? v.toString() : ""; }
    private Integer num(Object v) { return v != null ? ((Number) v).intValue() : 0; }
    private double parseDouble(String v) {
        if (v == null || v.isEmpty()) return 0;
        try { return Double.parseDouble(v.replace(",", ".")); } catch (Exception e) { return 0; }
    }
    private int parseInt(String v, int def) {
        if (v == null || v.isEmpty()) return def;
        try { int n = Integer.parseInt(v); return n > 0 ? n : def; } catch (Exception e) { return def; }
    }
    private String trim(String v) { return v != null ? v.trim() : ""; }
    private String esc(String v) { return v != null ? v.replace("'", "''") : ""; }
    private String sqlStr(String v) { return (v != null && !v.isEmpty()) ? "'" + esc(v) + "'" : "NULL"; }
    private String sqlNum(String v) { return (v != null && !v.isEmpty()) ? v : "NULL"; }
    private int boolInt(String v) { return "1".equals(v) ? 1 : 0; }
}
