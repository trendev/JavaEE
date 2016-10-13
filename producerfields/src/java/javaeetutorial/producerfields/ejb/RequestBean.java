/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package javaeetutorial.producerfields.ejb;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javaeetutorial.producerfields.db.UserDatabase;
import javaeetutorial.producerfields.entity.ToDo;
import javaeetutorial.producerfields.entity.ToDo_;
import javax.ejb.EJBException;
import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@ConversationScoped
@Stateful
public class RequestBean {

    @Inject
    @UserDatabase
    EntityManager em;

    /**
     * Returns all texts using JPA Criteria API
     *
     * @return the details of the TODO
     */
    public List<String> getAllTaskDetails() {

        CriteriaQuery<String> cq = em.getCriteriaBuilder().createQuery(String.class);

        Root<ToDo> pet = cq.from(ToDo.class);
        cq.select(pet.get(ToDo_.taskText));
        TypedQuery<String> q = em.createQuery(cq);
        return q.getResultList();
    }

    public ToDo createToDo(String inputString) {
        ToDo toDo;
        Date currentTime = Calendar.getInstance().getTime();

        try {
            toDo = new ToDo();
            toDo.setTaskText(inputString);
            toDo.setTimeCreated(currentTime);
            em.persist(toDo);
            return toDo;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    /**
     * Returns the TODO list using JPA JPQL request
     *
     * @return the TODO list.
     */
    public List<ToDo> getToDos() {
        try {
            List<ToDo> toDos
                    = (List<ToDo>) em.createQuery(
                            "SELECT t FROM ToDo t ORDER BY t.timeCreated")
                            .getResultList();
            return toDos;
        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

    /**
     * Will first check if the todo is in the context, if yes, removes it
     * otherwise merge it and then remove it.
     *
     * @param todo the task to remove
     */
    public void remove(ToDo todo) {
        try {
            if (em.contains(todo)) {
                em.remove(todo);
            } else {
                em.remove(em.merge(todo));
            }

        } catch (Exception e) {
            throw new EJBException(e.getMessage());
        }
    }

}
