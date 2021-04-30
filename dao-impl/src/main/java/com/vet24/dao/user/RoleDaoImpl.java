package com.vet24.dao.user;

import com.vet24.models.user.Role;

import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Role getRoleById(Long id) {
        Role role = entityManager.find(Role.class, id);
        return role;
    }

    @Override
    public List<Role> getAllRoles() {
        TypedQuery<Role> query =
                entityManager.createQuery("SELECT u FROM Role u", Role.class);
        return query.getResultList();
    }

    @Override
    public void addRole(Role role) {
        entityManager.persist(role);
    }

    @Override
    public void editRole(Role role) {
        entityManager.merge(role);
    }

    @Override
    public void deleteRole(Long id) {
        entityManager.remove(getRoleById(id));
    }
}