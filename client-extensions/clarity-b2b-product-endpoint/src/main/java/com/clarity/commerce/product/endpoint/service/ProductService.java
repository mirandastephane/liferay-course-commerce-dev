package com.clarity.commerce.product.endpoint.service;

import com.clarity.commerce.product.endpoint.model.ProductSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Aggregates product data from Liferay Headless Commerce APIs into a
 * {@link ProductSummary}.
 *
 * <p>Two API calls are needed:
 * <ol>
 *   <li>Admin Catalog {@code /v1.0/skus?search={sku}} — resolves
 *       price and productId for the SKU.</li>
 *   <li>Admin Catalog {@code /v1.0/products/{productId}} — resolves
 *       the product name and the {@code Wholesale Only} custom field.</li>
 * </ol>
 *
 * <p>Authentication uses HTTP Basic with the credentials injected at startup
 * via {@code LIFERAY_ADMIN_EMAIL_ADDRESS} / {@code LIFERAY_ADMIN_PASSWORD}
 * environment variables, or the defaults configured in {@code application.yaml}.
 * Replace with OAuth2 client credentials once the OAuth2 CET is registered.
 */
@Service
public class ProductService {

	private final RestClient restClient;

	@Value("${liferay.url:http://localhost:8080}")
	private String liferayUrl;

	@Value("${liferay.admin.email:ian.miller@clarityvisionsolutions.com}")
	private String adminEmail;

	@Value("${liferay.admin.password:learn}")
	private String adminPassword;

	public ProductService(RestClient.Builder restClientBuilder) {
		this.restClient = restClientBuilder.build();
	}

	/**
	 * Fetches and aggregates product data for the given SKU.
	 *
	 * @param sku the SKU code to look up (e.g. {@code "CVS-STD-SV-2003"})
	 * @return a {@link ProductSummary} with name, price, stockQuantity, and
	 *         wholesaleOnly aggregated from the Liferay Headless Commerce APIs,
	 *         or {@code null} if the SKU does not exist
	 */
	public ProductSummary getProductBySku(String sku) {

		// Step 1 — Resolve the SKU to a productId and price.
		Map skuResponse = restClient.get()
			.uri(liferayUrl + "/o/headless-commerce-admin-catalog/v1.0/skus"
				+ "?search=" + sku + "&pageSize=1")
			.header(HttpHeaders.AUTHORIZATION, basicAuth())
			.retrieve()
			.body(Map.class);

		List<Map> items = (List<Map>) skuResponse.get("items");
		if (items == null || items.isEmpty()) return null;
		Map skuItem = items.get(0);
		long productId = ((Number) skuItem.get("productId")).longValue();
		double price   = ((Number) skuItem.get("price")).doubleValue();

		// Step 2 — Resolve the product name and Wholesale Only custom field.
		Map productResponse = restClient.get()
			.uri(liferayUrl + "/o/headless-commerce-admin-catalog/v1.0/products/" + productId)
			.header(HttpHeaders.AUTHORIZATION, basicAuth())
			.retrieve()
			.body(Map.class);

		String name = ((Map<String, String>) productResponse.get("name")).get("en_US");

		List<Map> customFields = (List<Map>) productResponse.get("customFields");
		boolean wholesaleOnly = customFields.stream()
			.filter(f -> "Wholesale Only".equals(f.get("name")))
			.map(f -> (Map) f.get("customValue"))
			.map(cv -> (Boolean) cv.get("data"))
			.findFirst()
			.orElse(false);

		// Step 3 — Fetch total stock quantity across all warehouses.
		Map warehousesResponse = restClient.get()
			.uri(liferayUrl + "/o/headless-commerce-admin-inventory/v1.0/warehouses?pageSize=50")
			.header(HttpHeaders.AUTHORIZATION, basicAuth())
			.retrieve()
			.body(Map.class);

		int stockQuantity = 0;
		List<Map> warehouses = (List<Map>) warehousesResponse.get("items");
		if (warehouses != null) {
			for (Map warehouse : warehouses) {
				long warehouseId = ((Number) warehouse.get("id")).longValue();
				Map itemsResponse = restClient.get()
					.uri(liferayUrl + "/o/headless-commerce-admin-inventory/v1.0/warehouses/"
						+ warehouseId + "/warehouseItems?pageSize=200")
					.header(HttpHeaders.AUTHORIZATION, basicAuth())
					.retrieve()
					.body(Map.class);
				List<Map> warehouseItems = (List<Map>) itemsResponse.get("items");
				if (warehouseItems != null) {
					for (Map warehouseItem : warehouseItems) {
						if (sku.equals(warehouseItem.get("sku"))) {
							stockQuantity += ((Number) warehouseItem.get("quantity")).intValue();
						}
					}
				}
			}
		}

		return new ProductSummary(sku, name, price, stockQuantity, wholesaleOnly);
	}

	// ---------------------------------------------------------------------------
	// Helpers
	// ---------------------------------------------------------------------------

	/**
	 * Produces an HTTP Basic Authorization header value.
	 *
	 * <p>Replace with OAuth2 bearer token once the OAuth2 CET is registered.
	 * See README.md → Step 4 for OAuth2 configuration instructions.
	 */
	private String basicAuth() {
		String credentials = adminEmail + ":" + adminPassword;
		return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
	}

}
