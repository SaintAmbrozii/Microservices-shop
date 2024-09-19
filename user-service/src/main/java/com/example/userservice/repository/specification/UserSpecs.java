package com.example.userservice.repository.specification;


import com.example.userservice.domain.User;
import com.example.userservice.domain.criteria.UserSearchCriteria;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class UserSpecs {

    public static Specification<User> accordingToReportProperties(UserSearchCriteria criteria) {
        return (root, criteriaQuery, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (!ObjectUtils.isEmpty(criteria.getName()))
                predicates.add(cb.equal(root.get("name"), criteria.getName()));
            if (!ObjectUtils.isEmpty(criteria.getName()))
                predicates.add(cb.equal(root.get("lastname"), criteria.getLastname()));
            if (!ObjectUtils.isEmpty(criteria.getEmail()))
                predicates.add(cb.equal(root.get("email"), criteria.getEmail()));
            if (!ObjectUtils.isEmpty(criteria.getPhone()))
                predicates.add(cb.equal(root.get("phone"), criteria.getPhone()));
            if (!ObjectUtils.isEmpty(criteria.getEmail()))
                predicates.add(cb.equal(root.get("address"), criteria.getAddress()));

            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
