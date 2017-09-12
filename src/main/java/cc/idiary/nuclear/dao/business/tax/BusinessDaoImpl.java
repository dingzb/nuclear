package cc.idiary.nuclear.dao.business.tax;

import cc.idiary.nuclear.entity.business.tax.BusinessEntity;
import cc.idiary.nuclear.query.business.tax.BusinessQuery;
import cc.idiary.nuclear.dao.BaseDaoImpl;
import cc.idiary.nuclear.query.BaseQuery;
import cc.idiary.utils.common.StringTools;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Neo on 2017/5/9.
 */
@Repository
public class BusinessDaoImpl extends BaseDaoImpl<BusinessEntity> implements BusinessDao {
    public BusinessDaoImpl() {
        super(BusinessEntity.class);
    }

    @Override
    protected StringBuilder getExtensionSql(BaseQuery query, Map<String, Object> params) {

        BusinessQuery bQuery = (BusinessQuery) query;
        StringBuilder hqlsb = new StringBuilder();

        hqlsb.append(" from BusinessEntity {0}");

        if (bQuery.getHasIssue() != null) {
            hqlsb.append(" left join {0}.firstExamine first left join {0}.secondExamine second left join {0}.thirdExamine third ");
        }

        hqlsb.append(" where 1=1");

        if (bQuery.getHasIssue() != null) {
            hqlsb.append(" and (first.hasIssue = true or second.hasIssue = true or third.hasIssue = true)");
        }

        if (bQuery.getHasIssue() == null && bQuery.getFirstHasIssue() != null) {
            hqlsb.append(" and {0}.firstExamine.hasIssue = true");
        }
        if (bQuery.getHasIssue() == null && bQuery.getSecondHasIssue() != null) {
            hqlsb.append(" and {0}.secondExamine.hasIssue = true");
        }
        if (bQuery.getHasIssue() == null && bQuery.getThirdHasIssue() != null) {
            hqlsb.append(" and {0}.thirdExamine.hasIssue = true");
        }

        if (!StringTools.isEmpty(bQuery.getTaxpayerName())) {
            hqlsb.append(" and {0}.taxpayerName like :taxpayerName");
            params.put("taxpayerName", "%" + bQuery.getTaxpayerName() + "%");
        }
        if (!StringTools.isEmpty(bQuery.getTaxpayerCode())) {
            hqlsb.append(" and {0}.taxpayerCode like :taxpayerCode");
            params.put("taxpayerCode", "%" + bQuery.getTaxpayerCode() + "%");
        }
        if (!StringTools.isEmpty(bQuery.getAgencyId())) {
            hqlsb.append(" and {0}.agency.id = :agencyId");
            params.put("agencyId", bQuery.getAgencyId());
        }
        if (!StringTools.isEmpty(bQuery.getCategoryId())) {
            hqlsb.append(" and {0}.category.id = :categoryId");
            params.put("categoryId", bQuery.getCategoryId());
        }
        if (bQuery.getIncCategoryIds() != null && bQuery.getIncCategoryIds().length != 0) {
            hqlsb.append(" and {0}.category.id in (:categoryIds)");
            params.put("categoryIds", bQuery.getIncCategoryIds());
        }
        if (!StringTools.isEmpty(bQuery.getCategoryTypeId())) {
            hqlsb.append(" and {0}.category.type.id = :categoryTypeId");
            params.put("categoryTypeId", bQuery.getCategoryTypeId());
        }
        if (!StringTools.isEmpty(bQuery.getIssueId())) {
            hqlsb.append(" and {0}.issue.id = :issueId");
            params.put("issueId", bQuery.getIssueId());
        }

        if (bQuery.getCreateTimeStart() != null) {
            hqlsb.append(" and {0}.createTime >= :createTimeStart");
            params.put("createTimeStart", bQuery.getCreateTimeStart());
        }
        if (bQuery.getCreateTimeEnd() != null) {
            hqlsb.append(" and {0}.createTime <= :createTimeEnd");
            params.put("createTimeEnd", bQuery.getCreateTimeEnd());
        }

        if (bQuery.getBusTimeStart() != null) {
            hqlsb.append(" and {0}.busTime >= :busTimeStart");
            params.put("busTimeStart", bQuery.getBusTimeStart());
        }
        if (bQuery.getBusTimeEnd() != null) {
            hqlsb.append(" and {0}.busTime <= :busTimeEnd");
            params.put("busTimeEnd", bQuery.getBusTimeEnd());
        }

        if (bQuery.getStatus() != null) {
            hqlsb.append(" and {0}.status = :status");
            params.put("status", bQuery.getStatus());
        }
        if (bQuery.getIncludeStatus() != null) {
            hqlsb.append(" and {0}.status in (:statuses)");
            params.put("statuses", bQuery.getIncludeStatus());
        }
        if (!StringTools.isEmpty(bQuery.getCreateUserId())) {
            hqlsb.append(" and {0}.create.id = :create");
            params.put("create", bQuery.getCreateUserId());
        }
        if (!StringTools.isEmpty(bQuery.getCreateUserName())) {
            hqlsb.append(" and {0}.create.name like :createUserName");
            params.put("createUserName", "%" + bQuery.getCreateUserName() + "%");
        }
        if (bQuery.getCreateUserIds() != null) {
            hqlsb.append(" and {0}.create.id in (:creates)");
            params.put("creates", bQuery.getCreateUserIds());
        }

        if (!StringTools.isEmpty(bQuery.getAmendmentCode())){
            hqlsb.append(" and {0}.amendmentCode = :amendmentCode");
            params.put("amendmentCode", bQuery.getAmendmentCode());
        }
        if (bQuery.getIncAmendmentCode()!=null){
            hqlsb.append(" and {0}.amendmentCode in :amendmentCodes");
            params.put("amendmentCodes", bQuery.getIncAmendmentCode());
        }

        return hqlsb;
    }

    private String getOrderBy(BusinessQuery query, String alias) {
        StringBuilder sb = new StringBuilder(" order by");
        String sortField = query.getSort();
        if (sortField == null) {
            sortField = "busTime";
        }
        if ("createName".equals(sortField)) {
            sb.append(" {0}.create.name");
        } else if ("agencyName".equals(sortField)) {
            sb.append(" {0}.agency.name");
        } else if ("categoryName".equals(sortField)) {
            sb.append(" {0}.category.name");
        } else {
            sb.append(" {0}.").append(sortField);
        }
        sb.append(" ").append(StringTools.isEmpty(query.getOrder()) ? "desc" : query.getOrder());
        return MessageFormat.format(sb.toString(), alias);
    }

    @Override
    public List<BusinessEntity> paging(BusinessQuery query) {
        Map<String, Object> params = new HashMap<>();
        String hql = getHql(query, "business", params) + getOrderBy(query, "business");
        return getByHqlPaging(hql, params, query.getPage(), query.getSize());
    }

    @Override
    public List<BusinessEntity> pagingError(BusinessQuery query) {
        Map<String, Object> params = new HashMap<>();
        String hql = "select new BusinessEntity(business.id,business.taxpayerCode,business.taxpayerName,business.description,business.busTime,first.hasIssue,second.hasIssue,third.hasIssue,business.category,business.agency,business.create,business.createTime,business.modifyTime,business.status)"
                + getHql(query, "business", params) + " order by business.busTime desc";
        return getByHqlPaging(hql, params, query.getPage(), query.getSize());
    }
}
