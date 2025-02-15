import React, { useState } from "react";

import { useLocation, useNavigate } from "react-router-dom";
import AccountsService from "../services/accounts.service";
import "./PaymentComponent.css";

const PaymentComponent = () => {
  const [paymentId, setPaymentId] = useState(null);
  const location = useLocation();
  const navigate = useNavigate();
  const AccountService = new AccountsService();
  const amt = location.state.amount;
  const RazorpayURL = async () => {};
  const openRazorpay = async () => {
    const options = {
      key: "Api Key in new version is not provided that way we used razorpay UI page",
      amount: amt,
      currency: "INR",
      name: "NexBank",
      description: "Payment for services",
      handler: function (response) {
        setPaymentId(response.razorpay_payment_id);
        AccountService.makeCredit(
          location.state.id,
          amt,
          location.state.description
        );
        navigate(`/one-account/${location.state.id}`);
        //  Handle success/failure and other operations here
      },
    };
    // RazorpayURL();
    // const rzp = new window.Razorpay(options);
    //rzp.open();

     var paymentPageBaseUrl = 
       "https://pages.razorpay.com/pl_OhHzBq9KIxVa1N/view";
    var amount = amt;
    var email = "cdacchetangmail.com";
    var phone = "7722004099";

    AccountService.makeCredit(
      location.state.id,
      amt,
      location.state.description
    );
    var paymentUrl = `${paymentPageBaseUrl}?amount=${amount}&currency=INR&email=${email}&phone=${phone}&description=Payment%20for%20services`;
    window.open(paymentUrl, "_blank");
    navigate(`/one-account/${location.state.id}`);
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        height: "100vh",
      }}
    >
      <h2>
        Your account will be credited with {amt} Rs on successful transaction.
      </h2>
      <button onClick={openRazorpay} className="proceed-button">
        successfully Done{" "}
      </button>
      {paymentId && <p>Payment successful! Payment ID: {paymentId}</p>}
    </div>
  );
};

export default PaymentComponent;
