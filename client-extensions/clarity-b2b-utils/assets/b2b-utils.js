/**
 * Clarity B2B Utils
 *
 * Shared utility module for Distributor Portal fragments.
 * Import this module in fragments via the bareSpecifier:
 *
 *   import { fetchProductInventory, isLowStock, formatPrice } from 'clarity:b2b-utils';
 */

/**
 * Fetches the stock quantity for a given SKU from the Headless Commerce
 * Delivery Catalog API.
 *
 * @param {string} sku - The SKU code to look up (e.g. "CVS-STD-LNS-0110")
 * @param {number} channelId - The Commerce Channel ID to scope the request
 * @returns {Promise<number>} Resolves to the available stock quantity
 *
 * @example
 * const qty = await fetchProductInventory('CVS-STD-LNS-0110', 97521);
 * if (isLowStock(qty, 5)) {
 *   console.log('Running low!');
 * }
 */
export async function fetchProductInventory(sku, channelId) {
	const url = `/o/headless-commerce-delivery-catalog/v1.0/channels/${channelId}/products?search=${encodeURIComponent(sku)}&pageSize=1&nestedFields=skus`;
	const response = await fetch(url, { headers: { 'Accept': 'application/json' } });
	if (!response.ok) {
		throw new Error(`Inventory request failed: ${response.status}`);
	}
	const data = await response.json();
	const product = data.items?.[0];
	if (!product) {
		throw new Error(`SKU not found in channel ${channelId}: ${sku}`);
	}
	const skuEntry = product.skus?.find(s => s.sku === sku);
	if (!skuEntry) {
		throw new Error(`SKU not matched in product variant list: ${sku}`);
	}
	const stockQuantity = skuEntry.availability?.stockQuantity;
	if (stockQuantity === null || stockQuantity === undefined) {
		throw new Error(`No availability data for SKU ${sku} — enable "Display Availability" on the product`);
	}
	return stockQuantity;
}

/**
 * Determines whether a stock quantity is at or below the low-stock threshold.
 *
 * @param {number} quantity - The current available stock quantity
 * @param {number} threshold - The minimum acceptable stock level
 * @returns {boolean} True if `quantity` is positive and at or below `threshold`
 *
 * @example
 * isLowStock(3, 5);  // true
 * isLowStock(0, 5);  // false — out of stock is not the same as low stock
 * isLowStock(10, 5); // false
 */
export function isLowStock(quantity, threshold) {
	return quantity > 0 && quantity <= threshold;
}

/**
 * Formats a monetary amount for display using the browser's Intl API.
 * The locale is derived from the browser's language setting.
 *
 * @param {number} amount - The raw monetary amount (e.g. 55.00)
 * @param {string} currency - ISO 4217 currency code (e.g. "USD", "EUR", "GBP")
 * @returns {string} Locale-formatted price string (e.g. "$55.00")
 *
 * @example
 * formatPrice(55, 'USD');  // "$55.00"
 * formatPrice(49.5, 'EUR'); // "€49.50"
 */
export function formatPrice(amount, currency) {
	return new Intl.NumberFormat(navigator.language, {
		currency,
		style: 'currency',
	}).format(amount);
}
