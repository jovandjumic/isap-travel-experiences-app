package com.jovandjumic.isap_travel_experiences_app.Specification;

import com.jovandjumic.isap_travel_experiences_app.entities.Experience;
import org.springframework.data.jpa.domain.Specification;

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

    public static Specification<Experience> hasMinDaysSpent(Integer minDays) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("daysSpent"), minDays);
    }

    public static Specification<Experience> hasMaxDaysSpent(Integer maxDays) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("daysSpent"), maxDays);
    }

    public static Specification<Experience> hasMinTotalCost(Double minCost) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.join("costs").get("totalCost"), minCost);
    }

    public static Specification<Experience> hasMaxTotalCost(Double maxCost) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.join("costs").get("totalCost"), maxCost);
    }

    public static Specification<Experience> hasMinCostPerPerson(Double minCostPerPerson) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("costPerPerson"), minCostPerPerson);
    }

    public static Specification<Experience> hasMaxCostPerPerson(Double maxCostPerPerson) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("costPerPerson"), maxCostPerPerson);
    }

}
