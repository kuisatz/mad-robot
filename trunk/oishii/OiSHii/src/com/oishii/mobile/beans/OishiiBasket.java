package com.oishii.mobile.beans;

import java.util.ArrayList;
import java.util.List;

public class OishiiBasket {
	private List<BasketItem> items = new ArrayList<BasketItem>();
	private float currentTotal;
	private float discountedTotal;

	public float getCurrentTotal() {
		return currentTotal;
	}

	public List<BasketItem> getBasketItems() {
		return items;
	}

	public void setCurrentTotal(float currentTotal) {
		this.currentTotal = currentTotal;
	}

	private void updateTotal() {
		currentTotal = 0.0f;
		BasketItem item;
		for (int i = 0; i < items.size(); i++) {
			item = items.get(i);
			currentTotal += item.price * item.count;
		}
	}

	public void addItem(BasketItem item) {
		items.add(item);
		updateTotal();
	}

	public void removeItem(int index) {
		items.remove(index);
		updateTotal();
	}

	public void removeAllItems() {
		items.clear();
	}
}
