package com.bhasaka.fruitables.core.service;

import com.fasterxml.jackson.databind.node.ArrayNode;

public interface CountryStateService {
    ArrayNode getStates(String country);
}