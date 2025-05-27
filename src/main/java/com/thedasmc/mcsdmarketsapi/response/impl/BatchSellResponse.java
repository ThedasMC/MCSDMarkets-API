package com.thedasmc.mcsdmarketsapi.response.impl;

import java.util.List;
import java.util.Set;

public class BatchSellResponse {

    private List<CreateTransactionResponse> transactions;
    private Set<String> unsellableMaterials;

    public List<CreateTransactionResponse> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<CreateTransactionResponse> transactions) {
        this.transactions = transactions;
    }

    public Set<String> getUnsellableMaterials() {
        return unsellableMaterials;
    }

    public void setUnsellableMaterials(Set<String> unsellableMaterials) {
        this.unsellableMaterials = unsellableMaterials;
    }

    @Override
    public String toString() {
        return "BatchSellResponseDto{" +
            "transactions=" + transactions +
            ", unsellableMaterials=" + unsellableMaterials +
            '}';
    }
}
