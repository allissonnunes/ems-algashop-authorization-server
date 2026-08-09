package br.dev.allissonnunes.algashop.authorization_server.infrastructure.persistence;

import br.dev.allissonnunes.algashop.authorization_server.application.user.query.AuthUserFilter;
import br.dev.allissonnunes.algashop.authorization_server.application.user.query.AuthUserOutput;
import br.dev.allissonnunes.algashop.authorization_server.application.user.query.AuthUserQueryService;
import br.dev.allissonnunes.algashop.authorization_server.application.user.query.PageModel;
import br.dev.allissonnunes.algashop.authorization_server.domain.model.user.AuthUser;
import br.dev.allissonnunes.algashop.authorization_server.domain.model.user.AuthUserNotFoundException;
import br.dev.allissonnunes.algashop.authorization_server.domain.model.user.AuthUserRepository;
import br.dev.allissonnunes.algashop.authorization_server.domain.model.user.AuthUser_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthUserQueryServiceImpl implements AuthUserQueryService {

    private final AuthUserRepository authUserRepository;

    private final EntityManager entityManager;

    @Override
    public AuthUserOutput findById(final UUID userId) {
        return this.authUserRepository.findById(userId)
                .map(AuthUserOutput::from)
                .orElseThrow(() -> new AuthUserNotFoundException(userId));
    }

    @Override
    public PageModel<AuthUserOutput> findAll(final AuthUserFilter filter) {
        final Long total = countTotalQueryResults(filter);

        if (total.equals(0L)) {
            return PageModel.of(Page.empty(PageRequest.of(filter.page(), filter.size())));
        }

        return filterQuery(filter, total);
    }

    private Long countTotalQueryResults(final AuthUserFilter filter) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        final Root<AuthUser> root = query.from(AuthUser.class);

        query.select(builder.count(root))
                .where(toPredicates(builder, root, filter));

        return entityManager.createQuery(query).getSingleResult();
    }

    private PageModel<AuthUserOutput> filterQuery(final AuthUserFilter filter, final Long total) {
        final Pageable pageable = PageRequest.of(filter.page(), filter.size());
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<AuthUserOutput> query = builder.createQuery(AuthUserOutput.class);
        final Root<AuthUser> root = query.from(AuthUser.class);

        query.select(builder.construct(
                        AuthUserOutput.class,
                        root.get(AuthUser_.id),
                        root.get(AuthUser_.name),
                        root.get(AuthUser_.email),
                        root.get(AuthUser_.type),
                        root.get(AuthUser_.enabled)
                ))
                .where(toPredicates(builder, root, filter))
                .orderBy(toSortOrder(builder, root, filter));

        var typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        return PageModel.of(new PageImpl<>(typedQuery.getResultList(), pageable, total));
    }

    private Predicate[] toPredicates(final CriteriaBuilder builder, final Root<AuthUser> root, final AuthUserFilter filter) {
        final List<Predicate> predicates = new ArrayList<>();

        if (filter.email() != null) {
            predicates.add(builder.like(builder.lower(root.get(AuthUser_.email)),
                    "%" + filter.email().toLowerCase() + "%"));
        }

        if (filter.type() != null) {
            predicates.add(builder.equal(root.get(AuthUser_.type), filter.type()));
        }

        return predicates.toArray(new Predicate[0]);
    }

    private Order toSortOrder(final CriteriaBuilder builder, final Root<AuthUser> root, final AuthUserFilter filter) {
        return filter.direction().isAscending()
                ? builder.asc(root.get(filter.sort()))
                : builder.desc(root.get(filter.sort()));
    }

}
