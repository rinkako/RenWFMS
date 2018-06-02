/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renNameService;
import org.sysu.renCommon.interactionRouter.RInteractionRouter;
import org.sysu.renCommon.interactionRouter.RestfulRouter;
import org.sysu.renNameService.transaction.ConcurrentControlType;
import java.util.UUID;

/**
 * Usage : This class is used to store runtime context.
 */
public final class GlobalContext {

    /**
     * Name service micro-service global id.
     */
    public static final String NAME_SERVICE_GLOBAL_ID = String.format("WFMSComponent_NS_%s", UUID.randomUUID().toString());

    /**
     * Main internal interaction router.
     */
    public static final RInteractionRouter Interaction = new RestfulRouter();

    /**
     * Public key for data package signature.
     */
    public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCh5g4pc8+YD8QLXQSI441vkY+quXJfSS4m4QV4FTOCyTBR9rTqHGpvYRTITAV5nPHVFeej1c0+WSJ3DUopMdItuAMBdKUlvPigOQwWJShXl5IzlWGGYFC4tCAX34PyoXQ4ec/+Uj2YUA0b4/3t3HG077kpu13rGGKQ4uECtOyzAwIDAQAB";

    /**
     * Private key for data package signature.
     */
    public static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKHmDilzz5gPxAtdBIjjjW+Rj6q5cl9JLibhBXgVM4LJMFH2tOocam9hFMhMBXmc8dUV56PVzT5ZIncNSikx0i24AwF0pSW8+KA5DBYlKFeXkjOVYYZgULi0IBffg/KhdDh5z/5SPZhQDRvj/e3ccbTvuSm7XesYYpDi4QK07LMDAgMBAAECgYEAijkRJED1Hx1uwkjjQ0AMFBIRt6/mvwWKurTpZ3GqbeH5ODFKmooyMO+Qv8Vv8zmUmtm9z/oM5ktRipU1GOCpUPMJh6i14PMHY4gNB62uuvhkv2pAsHkfcYpkov4CIHVWtdyY+O0i0fyxMrq2ScjbURLlPUKUuj+sXunLI9Kz7RkCQQDmHLD95hZGdvTV/Qc0as+UvyXJJIOQvvaFlgSQQEO3zBiqnM/UYmNbtNFOHwnLyRwLpqhUz1vw/7ckDvX0oUZnAkEAtBzLHdZWGwER4U5j9ZfEw/T6+s87ta/9JgrSXFlfWetZBrnktwVV2laVNV54T/BL7L2xaOPrttrFLvJFRlU1BQJAAWhxiMwsnLfDnb+TmLWCmaVlxMpNZ8hTV9PoKT3LbEd6ayjUeLc1Zm0/zpuQAsgzPLsDUqmGJX5bD4Rr6thFxwJBAICGY3LhNgSDGO078RyZtnFW6Zn8M1GNSgnRgxcfN2mFBbxE/q6TghYR3tt8lEIG6UcAfg6pRdTv+/FqNIDh4LkCQBHszW0gobdBUFD79irxlUkBZiRtw1+SPI8FOwjJmy2Cx/Recv41pk3s3dq/FN6L0gQr5GsH2i7N8nWG2UJdYSE=";

    /**
     * Internal safe token for service requesting.
     */
    public static final String INTERNAL_TOKEN = "AUTH_admin@admin_2ac4a13d-872d-4c59-a1b1-b5c2ecfc2fb0-7df468cd-c9e0-49b4-ad08-5281bbbe4ce0";

    /**
     * Transaction action key in argument dict.
     */
    public static final String TRANSACTION_ACTION_KEY = "?action";

    /**
     * Name of administrator of a domain.
     */
    public static final String DOMAIN_ADMIN_NAME = "admin";

    /**
     * Transaction executor success flag string.
     */
    public static final String TRANSACTION_EXECUTOR_SUCCESS = "success";

    /**
     * Transaction executor fail flag string.
     */
    public static final String TRANSACTION_EXECUTOR_FAILED = "fail";

    /**
     * Size for log buffer.
     */
    public static final int LOG_BUFFER_SIZE = 0;

    /**
     * Size of scheduler concurrent executing transaction.
     */
    public static int CONCURRENT_MAX_EXECUTING_TRANSACTION = 100;

    /**
     * Is concurrent control is enabled.
     */
    public static ConcurrentControlType CONCURRENT_CONTROL_TYPE = ConcurrentControlType.None;

    /**
     * Valid duration of authorization token.
     */
    public static final Long AUTHORITY_TOKEN_VALID_SECOND = 2 * 60 * 60L;
}
