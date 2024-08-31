package com.jovandjumic.isap_travel_experiences_app.Specification;

import com.jovandjumic.isap_travel_experiences_app.entities.Destination;
import com.jovandjumic.isap_travel_experiences_app.entities.Experience;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class ExperienceSpecifications {

    public static Specification<Experience> hasUser(Long userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("appUser").get("id"), userId);
    }

    public static Specification<Experience> hasLocationName(String locationName) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(root.get("destination").get("locationName"), locationName);
    }

    public static Specification<Experience> hasRegionArea(String locationName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("destination").get("regionArea"), locationName);
    }

    public static Specification<Experience> hasCountry(String locationName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("destination").get("country").get("countryName"), locationName);
    }

    public static Specification<Experience> hasContinent(String locationName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("destination").get("country").get("continent"), locationName);
    }

    public static Specification<Experience> hasLocationType(String locationType) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("destination").get("locationType"), locationType);
    }

    public static Specification<Experience> hasDaysSpentBetween(Integer minDays, Integer maxDays) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("daysSpent"), minDays, maxDays);
    }

    public static Specification<Experience> hasTotalCostBetween(Double minCost, Double maxCost) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.join("costs").get("totalCost"), minCost, maxCost);
    }

    public static Specification<Experience> hasCostPerPersonBetween(Double minCostPerPerson, Double maxCostPerPerson) {
        return (root, query, criteriaBuilder) -> {
            Expression<Double> costPerPersonExpression = criteriaBuilder.function(
                    "getCostPerPerson",
                    Double.class,
                    root
            );
            return criteriaBuilder.between(costPerPersonExpression, minCostPerPerson, maxCostPerPerson);
        };
    }
}
