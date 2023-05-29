package com.demo.repositories.specification;

import com.demo.common.enums.AppStatus;
import com.demo.common.enums.Gender;
import com.demo.entities.Student;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class StudentSpecification {

  public Specification<Student> getStudent(
      String classId,
      Gender gender,
      String firstName,
      String lastName,
      String email,
      Long startDate,
      Long endDate,
      String searchKey,
      String sortField,
      boolean ascSort) {
    return (Root<Student> studentRoot, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
      cq.distinct(true);
      List<Predicate> predicates = new ArrayList<>();
      predicates.add(cb.equal(studentRoot.get("status"), AppStatus.ACTIVE));
      predicates.add(cb.equal(studentRoot.get("classId"), classId));

      if (email != null && !email.isEmpty()) {
        predicates.add(cb.equal(studentRoot.get("email"), email));
      }
      if (gender != null ) {
        predicates.add(cb.equal(studentRoot.get("gender"), gender));
      }
      if (firstName != null && !firstName.trim().isEmpty()) {
        predicates.add(cb.equal(studentRoot.get("firstName"), firstName));
      }
      if (lastName != null && !lastName.trim().isEmpty()) {
        predicates.add(cb.equal(studentRoot.get("lastName"), lastName));
      }
      if (startDate != null) {
        predicates.add(cb.greaterThanOrEqualTo(studentRoot.get("bob"), startDate));
      }
      if (endDate != null) {
        predicates.add(cb.lessThanOrEqualTo(studentRoot.get("bob"), endDate));
      }



      if (searchKey != null && !searchKey.trim().isEmpty()) {
        predicates.add(
            cb.or(
                cb.like(studentRoot.get("firstName"), "%" + searchKey.trim() + "%"),
                cb.like(studentRoot.get("lastName"), "%" + searchKey.trim() + "%"),
                cb.like(studentRoot.get("address"), "%" + searchKey.trim() + "%"),
                cb.like(studentRoot.get("email"), "%" + searchKey.trim() + "%")));
      }

      Path orderClause;
      switch (sortField) {
        case "firstName":
          orderClause = studentRoot.get("firstName");
          break;
        case "lastName":
          orderClause = studentRoot.get("lastName");
          break;
        case "bob":
          orderClause = studentRoot.get("bob");
          break;
        case "phoneNumber":
          orderClause = studentRoot.get("phoneNumber");
          break;
        default:
          orderClause = studentRoot.get("createdDate");
          break;
      }
      if (ascSort) {
        cq.orderBy(cb.asc(orderClause));
      } else {
        cq.orderBy(cb.desc(orderClause));
      }
      return cb.and(predicates.toArray(new Predicate[] {}));
    };
  }
}
