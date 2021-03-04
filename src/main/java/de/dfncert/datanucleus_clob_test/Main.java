package de.dfncert.datanucleus_clob_test;

import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Extent;
import javax.jdo.Query;
import javax.jdo.JDOHelper;
import javax.jdo.Transaction;

public class Main
{
    public static void main(String args[])
    {
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("Test");
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();

        try {
            tx.begin();

            if (args[0].equals("put")) {
                System.out.println("Putting object into database");

                Test test = null;
                if (args.length == 2) {
                    test = new Test(args[1]);
                } else {
                    test = new Test("foo");
                }

                pm.makePersistent(test);
                System.out.println(test);
            } else if (args[0].equals("get") && args.length == 1) {
                System.out.println("Getting all object from database");

                Query q = pm.newQuery("SELECT FROM " + Test.class.getName() + " ORDER BY id DESC");
                List<Test> tests = (List<Test>)q.execute();
                Iterator<Test> iter = tests.iterator();
                while (iter.hasNext()) {
                    System.out.println(iter.next());
                }
            } else if (args[0].equals("get") && args.length == 2) {
                System.out.println("Getting object with ID " + args[1] + " from database");

                Query q = pm.newQuery("SELECT FROM " + Test.class.getName() + " WHERE id == " + args[1]);
                List<Test> tests = (List<Test>)q.execute();
                Iterator<Test> iter = tests.iterator();
                if (iter.hasNext()) {
                    System.out.println(iter.next());
                }
            }

            tx.commit();
        } catch (Exception e) {
            System.out.println("Exception accessing database : " + e.getMessage());
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }
}
