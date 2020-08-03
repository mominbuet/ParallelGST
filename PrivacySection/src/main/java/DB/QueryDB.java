/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author azizmma
 */
public class QueryDB {

    final String factoryName = "GenomeHashTreePU";

    public <T> T insertGeneric(T p) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(factoryName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction entr = em.getTransaction();
        entr.begin();
        em.persist(p);

        entr.commit();

        return p;
    }

    public <T> void insertGeneric(List<T> list) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory(factoryName);
        EntityManager em = emf.createEntityManager();
        EntityTransaction entr = em.getTransaction();
        entr.begin();
        list.stream().forEach((p) -> {
            em.persist(p);
        });
        entr.commit();

    }

    public PrefixTreePlain updateRowId(PrefixTreePlain current_node, String rowID) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(factoryName);
            EntityManager em = emf.createEntityManager();
            EntityTransaction entr = em.getTransaction();
            entr.begin();
            current_node.setRowId(current_node.getRowId() + rowID + ",");
            entr.commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return current_node;
    }

    public SuffixTreePlain updateRowId(SuffixTreePlain current_node, String rowID) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(factoryName);
            EntityManager em = emf.createEntityManager();
            EntityTransaction entr = em.getTransaction();
            SuffixTreePlain tmp = em.find(SuffixTreePlain.class, current_node.getId());
            entr.begin();
            tmp.setRowId(current_node.getRowId() + rowID + ",");
            current_node.setRowId(current_node.getRowId() + rowID + ",");
            entr.commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return current_node;
    }

    public SuffixTreePlainMerkle updateHash(SuffixTreePlainMerkle current_node) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(factoryName);
            EntityManager em = emf.createEntityManager();
            EntityTransaction entr = em.getTransaction();
            SuffixTreePlainMerkle tmp = em.find(SuffixTreePlainMerkle.class, current_node.getId());
            entr.begin();
            tmp.setHashData(current_node.getHashData());
            entr.commit();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return current_node;
    }

    public void updateRowId(SuffixTreePlainMerkle current_node, int sequenceID, int position) {
//        try {
//            EntityManagerFactory emf = Persistence.createEntityManagerFactory(factoryName);
//            EntityManager em = emf.createEntityManager();
//            EntityTransaction entr = em.getTransaction();
//            SuffixTreePlainMerkle tmp = em.find(SuffixTreePlainMerkle.class, current_node.getId());
//            entr.begin();
//            tmp.setRowId(current_node.getRowId() + rowID + ",");
//            current_node.setRowId(current_node.getRowId() + rowID + ",");
//            entr.commit();
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//            return null;
//        }
//        return current_node;
        StreeMap streeMap = new StreeMap();
        streeMap.setPosition(position);
        streeMap.setSequenceId(sequenceID);
        streeMap.setStreeId(current_node.getId());
        insertGeneric(streeMap);
    }

    public List<PrefixTreePlain> getAllPrefixTreePlain() {
        List<PrefixTreePlain> res = new ArrayList<PrefixTreePlain>();
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(factoryName);
            EntityManager em = emf.createEntityManager();
            res = em.createNamedQuery("PrefixTreePlain.findAll", PrefixTreePlain.class).getResultList();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }

    public List<SuffixTreePlainMerkle> getAllSuffixTreePlainMerkle(int level) {
        List<SuffixTreePlainMerkle> res;
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(factoryName);
            EntityManager em = emf.createEntityManager();
            res = em.createNamedQuery("SuffixTreePlainMerkle.findByLevel", SuffixTreePlainMerkle.class).setParameter("level", level).getResultList();
            return res;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }

    }

    public PrefixTreePlain getPrefixTreePlain(int level, String data, PrefixTreePlain parentId) {
        PrefixTreePlain res = null;
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(factoryName);
            EntityManager em = emf.createEntityManager();
            List<PrefixTreePlain> tmpList = em.createNamedQuery("PrefixTreePlain.findByDataLevel", PrefixTreePlain.class)
                    .setParameter("level", level)
                    .setParameter("data", data).getResultList();
            for (PrefixTreePlain tmp : tmpList) {
                if (tmp.getParentId().equals(parentId.getId())) {
                    return tmp;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return res;
        }
        return res;
    }

    public SuffixTreePlainMerkle getSuffixTreePlain(int level, String data, Long parentId) {
        SuffixTreePlainMerkle res = null;
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(factoryName);
            EntityManager em = emf.createEntityManager();
            res = em.createNamedQuery("SuffixTreePlainMerkle.findByDataLevelParent", SuffixTreePlainMerkle.class)
                    .setParameter("level", level)
                    .setParameter("parentId", parentId)
                    .setParameter("data", data).getSingleResult();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        }
        return res;
    }

    public SuffixTreePlainMerkle getSuffixTreePlainMerkle(int level, String data, Integer parentId, boolean isLeaf) {
        SuffixTreePlainMerkle res = null;
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(factoryName);
            EntityManager em = emf.createEntityManager();
            res = em.createNamedQuery("SuffixTreePlainMerkle.findConstraint", SuffixTreePlainMerkle.class)
                    .setParameter("level", level).setParameter("parentId", parentId)
                    .setParameter("data", data).setParameter("isLeaf", isLeaf).getSingleResult();

        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
        }
        return res;
    }

    public SuffixTreePlain getSuffixTreePlain(int level, String data, SuffixTreePlain parentId) {
        SuffixTreePlain res = null;
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(factoryName);
            EntityManager em = emf.createEntityManager();
            List<SuffixTreePlain> tmpList = em.createNamedQuery("SuffixTreePlain.findByDataLevel", SuffixTreePlain.class)
                    .setParameter("level", level)
                    .setParameter("data", data).getResultList();
            for (SuffixTreePlain tmp : tmpList) {
                if (tmp.getParentId().equals(parentId.getId())) {
                    return tmp;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return res;
        }
        return res;
    }

    public SuffixTreePlainMerkle searchRootSuffixMerkle() {
        SuffixTreePlainMerkle res = null;
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(factoryName);
            EntityManager em = emf.createEntityManager();
            res = em.createNamedQuery("SuffixTreePlainMerkle.findByLevel", SuffixTreePlainMerkle.class).setParameter("level", -1).getSingleResult();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }

    public SuffixTreePlain searchRootSuffix() {
        SuffixTreePlain res = null;
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(factoryName);
            EntityManager em = emf.createEntityManager();
            res = em.createNamedQuery("SuffixTreePlain.findByLevel", SuffixTreePlain.class).setParameter("level", -1).getSingleResult();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }

    public PrefixTreePlain searchRoot() {
        PrefixTreePlain res = null;
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory(factoryName);
            EntityManager em = emf.createEntityManager();
            res = em.createNamedQuery("PrefixTreePlain.findByLevel", PrefixTreePlain.class).setParameter("level", -1).getSingleResult();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }

}
