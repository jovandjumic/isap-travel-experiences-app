package com.jovandjumic.isap_travel_experiences_app.Specification;

import com.jovandjumic.isap_travel_experiences_app.entities.Country;
import com.jovandjumic.isap_travel_experiences_app.entities.Destination;
import com.jovandjumic.isap_travel_experiences_app.entities.Experience;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
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

    public static Specification<Experience> hasCountryId(Long countryId) {
        return (root, query, criteriaBuilder) -> {
            Join<Experience, Destination> destinationJoin = root.join("destination");
            return criteriaBuilder.equal(destinationJoin.get("countryId"), countryId);
        };
    }

    public static Specification<Experience> hasContinent(String continent) {
        return (root, query, criteriaBuilder) -> {
            Join<Experience, Destination> destinationJoin = root.join("destination");
            // Ovo podrazumeva da je kontinent deo Country objekta u bazi, ali ga sada moraš dobiti preko countryId
            // Neophodno je da se u bazi veza prema kontinentu povuče kroz countryId
            assert query != null;
            Subquery<String> continentQuery = query.subquery(String.class);
            Root<Country> countryRoot = continentQuery.from(Country.class);
            continentQuery.select(countryRoot.get("continent"))
                    .where(criteriaBuilder.equal(countryRoot.get("id"), destinationJoin.get("countryId")));
            return criteriaBuilder.equal(continentQuery, continent);
        };
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
