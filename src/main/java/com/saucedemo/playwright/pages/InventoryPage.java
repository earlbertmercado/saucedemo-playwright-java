package com.saucedemo.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.playwright.base.BasePage;
import com.saucedemo.playwright.constants.HeaderLocators;
import com.saucedemo.playwright.constants.InventoryPageLocators;

import java.util.ArrayList;
import java.util.List;

public class InventoryPage extends BasePage {

    private final Locator pageTitle;
    private final Locator burgerMenu;
    private final Locator shoppingCart;
    private final Locator sortDropdown;
    private final Locator items;
    private final Locator itemNames;
    private final Locator itemPrices;
    private final Locator itemDescriptions;
    private final Locator itemImages;
    private final Locator addToCartButtons;
    private final Locator removeButtons;
    private final Locator cartBadge;

    public InventoryPage(Page page) {
        super(page);
        pageTitle           = locator(HeaderLocators.PAGE_TITLE);
        burgerMenu          = locator(HeaderLocators.BURGER_MENU_BUTTON);
        shoppingCart        = locator(HeaderLocators.SHOPPING_CART);
        sortDropdown        = locator(InventoryPageLocators.SORT_DROPDOWN);
        items               = locator(InventoryPageLocators.ITEMS);
        itemNames           = locator(InventoryPageLocators.ITEM_NAMES);
        itemPrices          = locator(InventoryPageLocators.ITEM_PRICES);
        itemDescriptions    = locator(InventoryPageLocators.ITEM_DESCRIPTIONS);
        itemImages          = locator(InventoryPageLocators.ITEM_IMAGES);
        addToCartButtons    = locator(InventoryPageLocators.ADD_TO_CART_BUTTONS);
        removeButtons       = locator(InventoryPageLocators.REMOVE_BUTTONS);
        cartBadge           = locator(HeaderLocators.SHOPPING_CART_BADGE);
    }

    // ------------------ Getter Methods ------------------
    public String getPageTitle() {
        return pageTitle.innerText();
    }

    public int getItemCount() {
        return items.count();
    }

    public String getItemName(int index) {
        return itemNames.nth(index).textContent().trim();
    }

    public String getItemDescription(int index) {
        return itemDescriptions.nth(index).textContent().trim();
    }

    public String getItemPrice(int index) {
        String priceText = itemPrices.nth(index).textContent().trim();
        return priceText.replace("$", "");
    }

    public String getItemImageSrc(int index) {
        return itemImages.nth(index).getAttribute("src");
    }

    public List<String> getItemNames() {
        List<String> names = itemNames.allTextContents();
        names.replaceAll(String::trim);
        return names;
    }

    public List<Double> getItemPrices() {
        List<String> priceStrings = itemPrices.allTextContents();
        List<Double> prices = new ArrayList<>();
        for (String priceStr : priceStrings) {
            prices.add(Double.parseDouble(priceStr.replace("$", "").trim()));
        }
        return prices;
    }

    public Locator getAddToCartButtons() {
        return addToCartButtons;
    }

    // ------------------ Visibility Methods ------------------
    public boolean isBurgerMenuVisible() {
        return burgerMenu.isVisible();
    }

    public boolean isShoppingCartVisible() {
        return shoppingCart.isVisible();
    }

    public boolean isSortDropdownVisible() {
        return sortDropdown.isVisible();
    }

    public boolean areItemsVisible() {
        return items.count() > 0;
    }

    public boolean areAddToCartButtonsVisible() {
        return addToCartButtons.count() == getItemCount();
    }

    // ------------------ Action Methods ------------------
    public CartPage clickShoppingCart() {
        shoppingCart.click();
        return new CartPage(page);
    }

    public ItemDetailPage clickSpecificItem(String locatorString) {
        locator(locatorString).click();
        return new ItemDetailPage(page);
    }

    public ItemDetailPage clickItemNameByIndex(int index) {
        itemNames.nth(index).click();
        return new ItemDetailPage(page);
    }

    public ItemDetailPage clickItemImageByIndex(int index) {
        itemImages.nth(index).click();
        return new ItemDetailPage(page);
    }

    public InventoryPage addItemToCartByIndex(int index) {
        String itemName = getItemName(index);
        logger.info("Adding item to cart: '{}' (Index: {})", itemName, index);

        if ("Add to cart".equals(addToCartButtons.nth(index).textContent().trim())) {
            addToCartButtons.nth(index).click();
        } else {
            logger.warn("Item '{}' is already in the cart or button state is incorrect.", itemName);
        }
        return this;
    }

    public InventoryPage removeItemFromCartByIndex(int index) {
        logger.info("Removing item from cart at index: {}", index);
        if ("Remove".equals(removeButtons.nth(index).textContent().trim())) {
            removeButtons.nth(index).click();
        }
        return this;
    }

    public int getCartItemCount() {
        if (cartBadge.isVisible()) {
            String count = cartBadge.textContent().trim();
            logger.debug("Shopping cart badge shows {} items.", count);
            return Integer.parseInt(count);
        }
        logger.debug("Shopping cart badge is not visible (Empty).");
        return 0;
    }

    // ------------------ Sorting Methods ------------------
    public InventoryPage sortByNameAsc() {
        logger.info("Sorting products by Name (A to Z)");
        sortDropdown.selectOption("az");
        return this;
    }

    public InventoryPage sortByNameDesc() {
        logger.info("Sorting products by Name (Z to A)");
        sortDropdown.selectOption("za");
        return this;
    }

    public InventoryPage sortByPriceAsc() {
        logger.info("Sorting products by Price (Low to High)");
        sortDropdown.selectOption("lohi");
        return this;
    }

    public InventoryPage sortByPriceDesc() {
        logger.info("Sorting products by Price (High to Low)");
        sortDropdown.selectOption("hilo");
        return this;
    }

    public boolean isSortedAlphabeticallyAsc() {
        List<String> names = getItemNames();
        List<String> sorted = new ArrayList<>(names);
        sorted.sort(String::compareTo);
        return names.equals(sorted);
    }

    public boolean isSortedAlphabeticallyDesc() {
        List<String> names = getItemNames();
        List<String> sorted = new ArrayList<>(names);
        sorted.sort((a, b) -> b.compareTo(a));
        return names.equals(sorted);
    }

    public boolean isSortedByPriceAsc() {
        List<Double> prices = getItemPrices();
        List<Double> sorted = new ArrayList<>(prices);
        sorted.sort(Double::compareTo);
        return prices.equals(sorted);
    }

    public boolean isSortedByPriceDesc() {
        List<Double> prices = getItemPrices();
        List<Double> sorted = new ArrayList<>(prices);
        sorted.sort((a, b) -> b.compareTo(a));
        return prices.equals(sorted);
    }

    // ------------------ Utility / Validation Methods ------------------
    public List<ItemInfo> getAllItemInfo() {
        List<ItemInfo> products = new ArrayList<>();
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            products.add(new ItemInfo(
                    getItemName(i),
                    getItemDescription(i),
                    getItemPrice(i),
                    getItemImageSrc(i)
            ));
        }
        return products;
    }

    public boolean areAllItemsValid() {
        logger.info("Performing bulk validation of all displayed items.");
        List<ItemInfo> products = getAllItemInfo();
        return !products.isEmpty() && products.stream().allMatch(info -> {
            boolean valid = info.isValid();
            if (!valid) logger.error("Item validation failed for: {}", info.name());
            return valid;
        });
    }

    // ------------------ DTO / Record ------------------
    public record ItemInfo(String name,
                           String description,
                           String price,
                           String imageSrc) {

        public boolean isValid() {
            return isNotBlank(name)
                    && isNotBlank(description)
                    && isValidPrice(price)
                    && isNotBlank(imageSrc);
        }

        private static boolean isNotBlank(String v) {
            return v != null && !v.isBlank();
        }

        private static boolean isValidPrice(String v) {
            try {
                Double.parseDouble(v.trim());
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}