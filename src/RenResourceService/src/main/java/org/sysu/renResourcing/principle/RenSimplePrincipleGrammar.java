/*
 * Project Ren @ 2018
 * Rinkako, Ariana, Gordan. SYSU SDCS.
 */
package org.sysu.renResourcing.principle;

import org.sysu.renCommon.enums.LogLevelType;
import org.sysu.renCommon.enums.WorkitemDistributionType;
import org.sysu.renResourcing.utility.LogUtil;
import org.sysu.renCommon.utility.SerializationUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Rinkako
 * Date  : 2018/2/9
 * Usage : A grammar parser for simplest principle descriptor. A descriptor
 *         in this grammar is in a pattern of string:<br/>
 *         <b>"DistributeType@DistributorName@ArgumentDictionaryForDistributorJSON@
 *         DictionaryOfConstraintsKeyWithItsParameterDictValue"</b><br/>
 *         While distribute type and name are case sensitive.<br/>
 *         For example, shortest queue allocation is described like this:<br/>
 *         <b>"Allocate@ShortestQueue@{}@{}"</b><br/>
 *         Another example, filter for offering can be described like this:<br/>
 *         <b>"Offer@QueueLength@{"length":"lt 3"}@{"CapabilityConstraint":{"contains":"cook"}}"</b><br/>
 *         The above descriptor means offer workitem to participant who has cook capability and their
 *         queue length should less than 3 workitems.<br/>
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
                HashMap<String, Map> constraints = SerializationUtil.JsonDeserialization(descriptorItems[3], HashMap.class);
                for (Map.Entry<String, Map> constraintKvp : constraints.entrySet()) {
                    retPrinciple.AddConstraint(constraintKvp.getKey(), new HashMap(constraintKvp.getValue()));
                }
            }
            return retPrinciple;
        }
        catch (Exception ex) {
            LogUtil.Log(String.format("Parse (%s) failed: %s", descriptor, ex),
                    RenSimplePrincipleGrammar.class.getName(), LogLevelType.ERROR, "");
            return null;
        }
    }
}
