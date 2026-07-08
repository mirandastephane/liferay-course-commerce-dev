package com.clarityvisionsolutions.commerce.pricing.external;

import com.liferay.commerce.pricing.model.CommercePriceModifier;
import com.liferay.commerce.pricing.model.CommercePriceModifierRel;
import com.liferay.commerce.pricing.service.CommercePriceModifierRelLocalService;
import com.liferay.commerce.pricing.type.CommercePriceModifierType;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.service.CPDefinitionLocalService;
import com.liferay.portal.kernel.exception.PortalException;

import java.math.BigDecimal;

import java.util.List;
import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author me
 */
@Component(
		property = {"commerce.price.modifier.type.key=clarity-external-pricing"},
		service = CommercePriceModifierType.class
)
public class ClarityCommerceExternalPriceModifier implements CommercePriceModifierType {

	@Reference
	private CommercePriceModifierRelLocalService
			_commercePriceModifierRelLocalService;

	@Reference
	private CPDefinitionLocalService _cpDefinitionLocalService;

	@Override
	public BigDecimal evaluate(
			BigDecimal price, CommercePriceModifier commercePriceModifier)
			throws PortalException {

		List<CommercePriceModifierRel> rels =
				_commercePriceModifierRelLocalService.getCommercePriceModifierRels(
						commercePriceModifier.getCommercePriceModifierId(),
						CPDefinition.class.getName());

		for (CommercePriceModifierRel rel : rels) {
			CPDefinition cpDefinition =
					_cpDefinitionLocalService.getCPDefinition(rel.getClassPK());

			Object wholesaleOnly =
					cpDefinition.getExpandoBridge().getAttribute("Wholesale Only");

			if (Boolean.TRUE.equals(wholesaleOnly)) {

				// Simulate external engine adjustment: 5% reduction

				BigDecimal adjustment = price.multiply(new BigDecimal("0.05"));

				return price.subtract(adjustment);
			}
		}

		return price;
	}

	@Override
	public String getKey() {
		return "clarity-external-pricing";
	}

	@Override
	public String getLabel(Locale locale) {
		return "Clarity External Price Modifier";
	}
}