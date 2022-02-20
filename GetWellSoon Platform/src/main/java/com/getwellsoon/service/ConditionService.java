package com.getwellsoon.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.SetJoin;
import javax.transaction.Transactional;

import com.getwellsoon.entity.Condition;
import com.getwellsoon.entity.Condition_;
import com.getwellsoon.entity.Trial;
import com.getwellsoon.entity.Trial_;
import com.getwellsoon.enumeration.EnrollmentStatus;
import com.getwellsoon.model.ConditionCountTO;
import com.getwellsoon.model.ConditionTO;
import com.getwellsoon.model.TrialMessage;
import com.getwellsoon.repository.ConditionRepository;
import com.getwellsoon.repository.TrialRepository;
import com.getwellsoon.util.TrialHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ConditionService {
	@Autowired
	private ConditionRepository conditionRepository;

	@PersistenceContext
	protected EntityManager eManager;

	protected CriteriaBuilder criteriaBuilder;
	// @Autowired
	// private TrialRepository trialRepository;

	public List<String> getListOfConditionsMatching (String name) {
		return conditionRepository.getNamesLike(name);
	}

	public List<TrialMessage> getTrialByConditionName(String name) {
		Condition foundCondition = conditionRepository.findOneByName(name);
		Set<Trial> trialsSet = foundCondition.getTrials();
		List<TrialMessage> trials = new ArrayList<TrialMessage>();
		trialsSet.stream().forEach(item -> {
			trials.add(TrialMessage.copy(item, true));
		});
		return trials;
	}

	public List<ConditionCountTO> getTop500() {
		List<ConditionCountTO> conditions = conditionRepository.getConditionWithActiveTrials();
		return conditions;
	}
	
	public List<ConditionTO> getAll() {
		List<ConditionTO> conditions = null;
		criteriaBuilder = eManager.getCriteriaBuilder();
		CriteriaQuery<ConditionTO> conditionCriteria = criteriaBuilder.createQuery(ConditionTO.class);
		Root<Condition> root = conditionCriteria.from(Condition.class);
		SetJoin<Condition, Trial> trialJoin = root.join(Condition_.trials, JoinType.INNER);
		
		Expression<Long> trialCountExpression = criteriaBuilder.count(trialJoin.get(Trial_.ID));

		List<Selection<?>> projectionItems = new ArrayList<Selection<?>>();
		projectionItems.add(root.get(Condition_.id));
		projectionItems.add(root.get(Condition_.name));
		projectionItems.add(trialCountExpression);

		CompoundSelection <ConditionTO> selection = criteriaBuilder.construct(ConditionTO.class, projectionItems.toArray(new Selection[0]));

		Predicate restriction = trialJoin.get(Trial_.ENROLLMENT_STATUS).in(TrialHelper.activeTrialStatus());
		conditionCriteria.select(selection).where(restriction).groupBy(root.get(Condition_.ID));
		TypedQuery<ConditionTO> conditionQuery = eManager.createQuery(conditionCriteria)
		.setMaxResults(300);

		conditions = conditionQuery.getResultList();

		/* Pageable batch = PageRequest.of(0, 100);
		Page<Condition> batchPage = conditionRepository.findAllConditions(batch, statusList);
		List<Condition> pageData = batchPage.getContent();
		conditions = pageData.stream().map(item -> new ConditionTO(item.getId(), item.getName(), item.getTrials().size())).collect(Collectors.toList()); */
		return conditions;
		// return conditionRepository.findAll();
	}

	public List<Condition> getBySlug(String slug) {
		return conditionRepository.findBySlug(slug);
	}

	/* public Tuple getTrialAggregatedCondition() {
		criteriaBuilder = eManager.getCriteriaBuilder();
		CriteriaQuery query = criteriaBuilder.createTupleQuery();
		Root<Condition> conditionRoot = query.from(Condition.class);

	} */
}
