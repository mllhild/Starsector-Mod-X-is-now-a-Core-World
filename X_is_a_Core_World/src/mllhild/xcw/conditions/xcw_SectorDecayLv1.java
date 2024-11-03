package mllhild.xcw.conditions;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketConditionAPI;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;
import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.util.List;

public class xcw_SectorDecayLv1 extends BaseMarketConditionPlugin{
    public static float INCOME_MULT = -0.10f;
    public static float ACCESSIBILITY_MOD = -0.10f;
    public static int STABILITY_BONUS = -1;
    public static int SUPPLY_BONUS = -1;
    @Override
    public void apply(String id) {
        List<Industry> inds = this.market.getIndustries();
        for(Industry industry : inds){
            List<MutableCommodityQuantity> mcqs = industry.getAllSupply();
            for(MutableCommodityQuantity mcq : mcqs){
                if(mcq.getCommodityId().equalsIgnoreCase("ships")){
                    industry.getSupply("ships").getQuantity().modifyFlat(id, SUPPLY_BONUS, "Sector Decay Lv1");
                }
                if(mcq.getCommodityId().equalsIgnoreCase("fuel")){
                    industry.getSupply("fuel").getQuantity().modifyFlat(id, SUPPLY_BONUS, "Sector Decay Lv1");
                }
                if(mcq.getCommodityId().equalsIgnoreCase("heavy_machinery")){
                    industry.getSupply("heavy_machinery").getQuantity().modifyFlat(id, SUPPLY_BONUS, "Sector Decay Lv1");
                }
                if(mcq.getCommodityId().equalsIgnoreCase("rare_metals")){
                    industry.getSupply("rare_metals").getQuantity().modifyFlat(id, SUPPLY_BONUS, "Sector Decay Lv1");
                }
            }
        }
        this.market.getIncomeMult().modifyMult(id, 1f + INCOME_MULT, "Sector Decay Lv1");
        this.market.getAccessibilityMod().modifyFlat(id, ACCESSIBILITY_MOD, "Sector Decay Lv1");
        this.market.getStability().modifyFlat(id, STABILITY_BONUS, "Sector Decay Lv1");
    }

    @Override
    public void unapply(String id) {
        this.market.getStability().unmodify(id);
        this.market.getIncomeMult().unmodify(id);
        this.market.getAccessibilityMod().unmodify(id);
        List<Industry> inds = this.market.getIndustries();
        for(Industry industry : inds){
            industry.getSupplyBonusFromOther().unmodify(id);
        }
    }

    @Override
    protected void createTooltipAfterDescription(TooltipMakerAPI tooltip, boolean expanded) {
        tooltip.addSpacer(8f);
        tooltip.addPara("%s income", 0f, Misc.getHighlightColor(), " " + Math.round(INCOME_MULT * 100f) + "%");
        tooltip.addSpacer(8f);
        tooltip.addPara("%s accessibility", 0f, Misc.getHighlightColor(), " " + Math.round(ACCESSIBILITY_MOD * 100f) + "%");
        tooltip.addSpacer(8f);
        tooltip.addPara("%s stability", 0f, Misc.getHighlightColor(), " " + STABILITY_BONUS);
        tooltip.addSpacer(8f);
        tooltip.addPara("%s production to ship hulls, heavy machinery, rare metals, fuel", 0f, Misc.getHighlightColor(), " " + SUPPLY_BONUS);
    }
}
