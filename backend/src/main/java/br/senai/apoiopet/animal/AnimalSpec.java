package br.senai.apoiopet.animal;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AnimalSpec {

    private AnimalSpec() {}

    public static Specification<Animal> build(AnimalFiltroDTO f) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (f.especie() != null && !f.especie().isBlank()) {
                try { predicates.add(cb.equal(root.get("especie"), Especie.valueOf(f.especie()))); }
                catch (IllegalArgumentException ignored) {}
            }
            if (f.porte() != null && !f.porte().isBlank()) {
                try { predicates.add(cb.equal(root.get("porte"), Porte.valueOf(f.porte()))); }
                catch (IllegalArgumentException ignored) {}
            }
            if (f.faixaEtaria() != null && !f.faixaEtaria().isBlank()) {
                try { predicates.add(cb.equal(root.get("faixaEtaria"), FaixaEtaria.valueOf(f.faixaEtaria()))); }
                catch (IllegalArgumentException ignored) {}
            }
            if (f.sexo() != null && !f.sexo().isBlank()) {
                try { predicates.add(cb.equal(root.get("sexo"), Sexo.valueOf(f.sexo()))); }
                catch (IllegalArgumentException ignored) {}
            }
            if (f.status() != null && !f.status().isBlank()) {
                try { predicates.add(cb.equal(root.get("status"), AnimalStatus.valueOf(f.status()))); }
                catch (IllegalArgumentException ignored) {}
            }
            if (f.cidade() != null && !f.cidade().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("cidade")),
                        "%" + f.cidade().toLowerCase() + "%"
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
