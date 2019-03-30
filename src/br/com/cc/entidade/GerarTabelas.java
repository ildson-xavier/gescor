

package br.com.cc.entidade;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class GerarTabelas {
    
    public static void main (String []args){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("ccomissao");
        
        factory.close();
    }
}
