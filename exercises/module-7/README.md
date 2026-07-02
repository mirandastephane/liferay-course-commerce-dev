# Module 7 — Artifacts

This directory contains the pre-scaffolded artifacts for Module 7:
Storefront Configuration, Extension, and Customization.

## Artifacts

| Artifact | Type | Directory |
|---|---|---|
| clarity-b2b-product-endpoint | Client Extension (Microservice) | client-extensions/ |
| clarity-b2b-utils | Client Extension (JS Import Map) | client-extensions/ |
| clarity-featured-product | Fragment | fragments/ |
| clarity-wishlist | Fragment | fragments/ |

## Prerequisites

- Module 3: B2B Distributors Account Group and accounts configured
- Module 4: Clarity Eyewear catalog with products and Wholesale Only custom field
- Module 5: Clarity B2B Price List configured
- Module 6: Warehouses and Low Stock Thresholds configured

## Deploy Order

1. clarity-b2b-utils (JS Import Map — required by fragments)
2. clarity-b2b-product-endpoint (Client Extension)
3. clarity-featured-product (Fragment — requires clarity-b2b-utils)
4. clarity-wishlist (Fragment — requires clarity-b2b-utils)
