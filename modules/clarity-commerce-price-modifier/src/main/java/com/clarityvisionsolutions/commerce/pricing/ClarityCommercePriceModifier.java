package com.clarityvisionsolutions.commerce.pricing;

import com.liferay.commerce.currency.service.CommerceCurrencyLocalService;
import com.liferay.commerce.pricing.model.CommercePriceModifier;
import com.liferay.commerce.pricing.type.CommercePriceModifierType;

import com.liferay.portal.kernel.exception.PortalException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * @author me
 */
@Component(
	property = {
		"commerce.price.modifier.type.key=clarity-price-modifier"
	},
	service = CommercePriceModifierType.class
)
public class ClarityCommercePriceModifier implements CommercePriceModifierType {

	@Reference
	private CommerceCurrencyLocalService _commerceCurrencyLocalService;

	@Override
	public BigDecimal evaluate(BigDecimal price, CommercePriceModifier commercePriceModifier)
			throws PortalException {

		return price;
	}

	@Override
	public String getKey() {
		return "clarity-price-modifier";
	}

	@Override
	public String getLabel(Locale locale) {
		return "Clarity Price Modifier";
	}
}