package com.example.ecommerce.service;

import com.example.ecommerce.models.order.check_out.CheckoutRequest;
import com.example.ecommerce.payload.ResponseBody;

public interface CheckoutService {

    ResponseBody customerGetCheckOut(CheckoutRequest req);

    ResponseBody customerSubmitCheckOut(CheckoutRequest req);
}
