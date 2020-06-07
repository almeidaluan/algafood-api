package com.algaworks.algafood.infrastructure.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public List<Restaurante> find(String nome, 
			BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		
		var jpql = new StringBuilder();
		jpql.append("from Restaurante where 0 = 0 ");
		
		var parametros = new HashMap<String, Object>();
		
		if (StringUtils.hasLength(nome)) {
			jpql.append("and nome like :nome ");
			parametros.put("nome", "%" + nome + "%");
		}
		
		if (taxaFreteInicial != null) {
			jpql.append("and taxaFrete >= :taxaInicial ");
			parametros.put("taxaInicial", taxaFreteInicial);
		}
		
		if (taxaFreteFinal != null) {
			jpql.append("and taxaFrete <= :taxaFinal ");
			parametros.put("taxaFinal", taxaFreteFinal);
		}
		
		TypedQuery<Restaurante> query = manager
				.createQuery(jpql.toString(), Restaurante.class);
		
		parametros.forEach((chave, valor) -> query.setParameter(chave, valor));

		return query.getResultList();
	}

//	public List<Restaurante> findWithCriteria(String nome,
////								  BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal){
////		var criteriaBuilder = manager.getCriteriaBuilder();
////
////		CriteriaQuery<Restaurante> criteria = criteriaBuilder.createQuery(Restaurante.class);
////
////		criteria.from(Restaurante.class);
////
////		TypedQuery<Restaurante> query = manager.createQuery(criteria);
////
////		return query.getResultList();

//	}
		public List<Restaurante> findWithCriteria(String nome,
								  BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal){
		var criteriaBuilder = manager.getCriteriaBuilder();

		CriteriaQuery<Restaurante> criteria = criteriaBuilder.createQuery(Restaurante.class);

		var  root = criteria.from(Restaurante.class);

		Predicate nomePredicate = criteriaBuilder.like(root.get("nome"), "%" + nome + "%");

		Predicate taxaInicialPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("taxaFrete"),taxaFreteInicial);

		Predicate taxaFinalPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("taxaFrete"),taxaFreteFinal);

		criteria.where(nomePredicate,taxaInicialPredicate,taxaFinalPredicate);

		TypedQuery<Restaurante> query = manager.createQuery(criteria);

		return query.getResultList();

	}

}
