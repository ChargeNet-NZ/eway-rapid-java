package nz.net.charge.eway.rapid.sdk.beans.external;

/**
 * Describe the shipping method used to ship an order
 */
public enum ShippingMethod {
    Unknown,
    LowCost,
    DesignatedByCustomer,
    International,
    Military,
    NextDay,
    StorePickup,
    TwoDayService,
    ThreeDayService,
    Other
}
