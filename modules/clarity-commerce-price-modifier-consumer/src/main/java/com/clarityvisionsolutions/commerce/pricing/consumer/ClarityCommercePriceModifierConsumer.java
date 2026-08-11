package com.clarityvisionsolutions.commerce.pricing.consumer;

import com.liferay.commerce.pricing.type.CommercePriceModifierType;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author me
 */
@Component(
		immediate = true,
		service = ClarityCommercePriceModifierConsumer.class)


public class ClarityCommercePriceModifierConsumer {

	private static final Log _log = LogFactoryUtil.getLog(
			ClarityCommercePriceModifierConsumer.class);

	@Reference(target = "(commerce.price.modifier.type.key=clarity-price-modifier)")
	private CommercePriceModifierType _clarityPriceModifierType;

	@Activate
	protected void activate() {
		_log.info("Activated — provider key: clarity-price-modifier");
	}

	@Deactivate
	protected void deactivate() {
		_log.info("Deactivated — provider gone");
	}

}