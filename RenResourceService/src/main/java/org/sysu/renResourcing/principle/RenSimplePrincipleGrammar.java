/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.principle;

import org.sysu.renResourcing.basic.enums.WorkitemDistributionType;
import org.sysu.renResourcing.utility.LogUtil;
import org.sysu.renCommon.utility.SerializationUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author: Rinkako
 * Date  : 2018/2/9
 * Usage : A grammar parser for simplest principle descriptor. A descriptor
 *         in this grammar is in a pattern of string:<br/>
 *         <b>"DistributeType@DistributorName@ArgumentDictionaryForDistributorJSON@
 *         ListOfConstraintJSON@ListOfArgumentDictionaryForConstrainsJSON"</b><br/>
 *         While distribute type and name are case sensitive.<br/>
 *         For example, shortest queue allocation is described like this:<br/>
 *         <b>"Allocate@ShortestQueue@{}@[]@[]"</b><br/>
 *         Another example, filter for offering can be described like this:<br/>
 *         <b>"Offer@QueueLength@{"cond": "lt 10"}@["OfferCount"]@[{"cond": "eq 3"}]"</b><br/>
 *         The above descriptor means offer workitem to 3 participants and their
 *         queue length should less then 10 workitems.<br/>
 *         About the valid distribute type, distributor name and their parameters
 *         can see the project document.
 */
public class RenSimplePrincipleGrammar implements PrincipleGrammar {

    /**
     * Parse a principle descriptor to a principle object.
     * @param descriptor principle descriptor string
     * @return parsed principle object, null if parse failure
     */
    @Override
    public RPrinciple Parse(String descriptor) {
        try {
            assert descriptor != null;
            String[] descriptorItems = descriptor.split("@");
            assert descriptorItems.length >= 2;
            RPrinciple retPrinciple = new RPrinciple();
            WorkitemDistributionType distributionType = WorkitemDistributionType.valueOf(descriptorItems[0]);
            HashMap filterArgs = descriptorItems.length >= 3 ? SerializationUtil.JsonDeserialization(descriptorItems[2], HashMap.class) : new HashMap();
            retPrinciple.SetParsed(distributionType, descriptorItems[1], filterArgs);
            if (descriptorItems.length > 3) {
                ArrayList constraints = SerializationUtil.JsonDeserialization(descriptorItems[3], ArrayList.class);
                ArrayList constraintArgs = SerializationUtil.JsonDeserialization(descriptorItems[4], ArrayList.class);
                assert constraints != null && constraintArgs != null && constraintArgs.size() == constraints.size();
                for (int i = 0; i < constraints.size(); i++) {
                    retPrinciple.AddConstraint((String) constraints.get(i), (HashMap) constraintArgs.get(i));
                }
            }
            return retPrinciple;
        }
        catch (Exception ex) {
            // todo use interfaceX to report this exception to domain ren auth user
            LogUtil.Log(String.format("Parse (%s) failed: %s", descriptor, ex),
                    RenSimplePrincipleGrammar.class.getName(), LogUtil.LogLevelType.ERROR, "");
            return null;
        }
    }
}
