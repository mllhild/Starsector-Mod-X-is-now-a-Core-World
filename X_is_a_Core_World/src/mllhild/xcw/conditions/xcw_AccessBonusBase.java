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

public class xcw_AccessBonusBase extends BaseMarketConditionPlugin{
    public static float ACCESSIBILITY_MOD = 0.00f;
    @Override
    public void apply(String id) {
        this.market.getAccessibilityMod().modifyFlat(id, ACCESSIBILITY_MOD, "X Core Access Bonus Base");
    }

    @Override
    public void unapply(String id) {
        this.market.getAccessibilityMod().unmodify(id);
    }

    @Override
    protected void createTooltipAfterDescription(TooltipMakerAPI tooltip, boolean expanded) {
        tooltip.addSpacer(8f);
        tooltip.addPara("Adds accessibility to offset distance from core due to reshuffle.", 10 );
        tooltip.addSpacer(8f);
        tooltip.addPara("%s accessibility Bonus", 0f, Misc.getHighlightColor(), " " + Math.round(ACCESSIBILITY_MOD * 100f) + "%");
        tooltip.addSpacer(8f);
    }
}
