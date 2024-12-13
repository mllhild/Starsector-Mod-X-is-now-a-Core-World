package mllhild.xcw;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.campaign.StarSystem;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import com.fs.starfarer.api.impl.campaign.terrain.BaseTiledTerrain;
import com.fs.starfarer.api.impl.campaign.procgen.NebulaEditor;
import com.fs.starfarer.api.impl.campaign.terrain.HyperspaceTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import lunalib.lunaSettings.LunaSettings;
import org.lwjgl.util.vector.Vector2f;

public class xcw_MoveInhabitedNonCoreSystems {
    private static final Logger log = Logger.getLogger(xcwModPlugin.class);

    public static void cleanup(final StarSystemAPI system) {
        final HyperspaceTerrainPlugin plugin = (HyperspaceTerrainPlugin)Misc.getHyperspaceTerrain().getPlugin();
        final NebulaEditor editor = new NebulaEditor((BaseTiledTerrain)plugin);
        final float minRadius = plugin.getTileSize() * 0.5f;
        final float radius = system.getMaxRadiusInHyperspace();
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0.0f, radius + minRadius * 0.5f, 0.0f, 360.0f);
        editor.clearArc(system.getLocation().x, system.getLocation().y, 0.0f, radius + minRadius, 0.0f, 360.0f, 0.25f);
    }

    public void LunaStart(){
        log.info("X_is_a_Core_World");
        int radiusMax = LunaSettings.getInt("xcw","radiusMax");
        log.info(radiusMax);
        int radiusMin = LunaSettings.getInt("xcw","radiusMin");
        log.info(radiusMin);
        String xcwRadio = LunaSettings.getString("xcw","xcwRadio");
        log.info(xcwRadio);

        int distanceInhabitedMin = LunaSettings.getInt("xcw","interInhabitedSystemDistanceMin");
        log.info(distanceInhabitedMin);
        int distanceUninhabitedMin = LunaSettings.getInt("xcw","interNonInhabitedSystemDistanceMin");
        log.info(distanceUninhabitedMin);

        Boolean accessbonusInhabited = LunaSettings.getBoolean("xcw","accessbonusBoolean");
        log.info(accessbonusInhabited);
        Boolean accessbonusallAll = LunaSettings.getBoolean("xcw","accessbonusallBoolean");
        log.info(accessbonusallAll);



        switch (xcwRadio){
            case "Move_Inhabited_To_Core":
                log.info("Move_Inhabited_To_Core start");
                Destribution_Move_Inhabited_To_Core();
                AddAccessBonusToPlanets();
                log.info("Move_Inhabited_To_Core end");
                break;
            case "All_Random_To_Core":
                log.info("All_Random_To_Core start");
                Destribution_All_Random_To_Core();
                AddAccessBonusToPlanets();
                log.info("All_Random_To_Core end");
                break;
            case "All_Random":
                log.info("All_Random start");
                Destribution_All_Random();
                AddAccessBonusToPlanets();
                log.info("All_Random end");
                break;
            default:
                log.info("None");
                break;
        }
    }

    public void IniciateDecay(int decaySpeed){

    }

    public void Destribution_Move_Inhabited_To_Core(){
        int radiusMax = LunaSettings.getInt("xcw","radiusMax");
        int radiusMin = LunaSettings.getInt("xcw","radiusMin");
        for(StarSystemAPI system: Global.getSector().getStarSystems()){
            boolean systemIsInhabitated = false;
            for(PlanetAPI planet: system.getPlanets()){
                MarketAPI market = planet.getMarket();
                if (market == null)
                    continue;
                int marketSize = market.getSize();
                if(marketSize > 1){
                    systemIsInhabitated = true;
                    break;
                    }
            }
            if(systemIsInhabitated)
                SetNewSystemPosition(radiusMin, radiusMax, system, 5000, 5000, systemIsInhabitated);
        }

    }

    public void Destribution_All_Random_To_Core(){
        int radiusMax = LunaSettings.getInt("xcw","radiusMax");
        int radiusMin = LunaSettings.getInt("xcw","radiusMin");
        for(StarSystemAPI system: Global.getSector().getStarSystems()){
            boolean systemIsInhabitated = false;
            for(PlanetAPI planet: system.getPlanets()){
                if(planet.getFaction() == null)
                    continue;
                if(planet.getFaction().getId().equalsIgnoreCase("neutral"))
                    continue;
                systemIsInhabitated = true;
                break;
            }
            if(systemIsInhabitated)
                SetNewSystemPosition(radiusMin, radiusMax, system, 0, 0, systemIsInhabitated);
        }
    }

    public void Destribution_All_Random(){
        int distanceInhabitedMin = LunaSettings.getInt("xcw","interInhabitedSystemDistanceMin");
        int distanceUninhabitedMin = LunaSettings.getInt("xcw","interNonInhabitedSystemDistanceMin");
        List<StarSystemAPI> inhabitedSystems = new ArrayList<StarSystemAPI>();
        for(StarSystemAPI system: Global.getSector().getStarSystems()){
            float x = (ThreadLocalRandom.current().nextFloat() - 0.5f)*2*80000;
            float y = (ThreadLocalRandom.current().nextFloat() - 0.5f)*2*40000;
            system.getLocation().set(x,y);
        }
        for(StarSystemAPI system: Global.getSector().getStarSystems()){
            boolean systemIsInhabitated = false;
            for(PlanetAPI planet: system.getPlanets()){
                MarketAPI market = planet.getMarket();
                if (market == null)
                    continue;
                int marketSize = market.getSize();
                if(marketSize > 1){
                    systemIsInhabitated = true;
                    break;
                }
            }
            int attemptCounter = 0;
            if(systemIsInhabitated)
                while(DistanceBetweenSystems(system, GetClosestSystem(system)) < distanceInhabitedMin){
                    float x = (ThreadLocalRandom.current().nextFloat() - 0.5f)*2*80000;
                    float y = (ThreadLocalRandom.current().nextFloat() - 0.5f)*2*40000;
                    system.getLocation().set(x,y);
                    attemptCounter++;
                    if(attemptCounter > 10) break;
                }
            if(!systemIsInhabitated)
                while(DistanceBetweenSystems(system, GetClosestSystem(system)) < distanceUninhabitedMin){
                    float x = (ThreadLocalRandom.current().nextFloat() - 0.5f)*2*80000;
                    float y = (ThreadLocalRandom.current().nextFloat() - 0.5f)*2*40000;
                    system.getLocation().set(x,y);
                    attemptCounter++;
                    if(attemptCounter > 10) break;
                }
        }
        for(StarSystemAPI system: Global.getSector().getStarSystems())
            cleanup(system);
    }

    public void AddAccessBonusToPlanets(){
        Boolean accessbonusInhabited = LunaSettings.getBoolean("xcw","accessbonusBoolean");
        Boolean accessbonusallAll = LunaSettings.getBoolean("xcw","accessbonusallBoolean");
        for(StarSystemAPI system: Global.getSector().getStarSystems()){
            for(PlanetAPI planet: system.getPlanets()){
                MarketAPI market = planet.getMarket();
                if (market == null)
                    continue;
                if(market.getSize() == 1)
                    if(!accessbonusallAll)
                        continue;
                if(market.getSize() > 1)
                    if(!accessbonusInhabited)
                        continue;

                float distance = (float) Math.sqrt( Math.pow(system.getLocation().getX(), 2) + Math.pow(system.getLocation().getY(), 2) );

                if (distance < 5000) {
                    // Nothing to do
                } else if (distance < 10000) {
                    market.addCondition("xcw_access_lv1");
                } else if (distance < 20000) {
                    market.addCondition("xcw_access_lv2");
                } else if (distance < 30000) {
                    market.addCondition("xcw_access_lv3");
                } else if (distance < 40000) {
                    market.addCondition("xcw_access_lv4");
                } else if (distance < 50000) {
                    market.addCondition("xcw_access_lv5");
                } else if (distance < 60000) {
                    market.addCondition("xcw_access_lv6");
                } else if (distance < 70000) {
                    market.addCondition("xcw_access_lv7");
                }
            }
        }
    }

    public void SetNewSystemPosition(int radiusMin, int radiusMax, StarSystemAPI system, int bonusX, int bunusY, Boolean isInhabited){
        int distanceInhabitedMin = LunaSettings.getInt("xcw","interInhabitedSystemDistanceMin");
        int distanceUninhabitedMin = LunaSettings.getInt("xcw","interNonInhabitedSystemDistanceMin");
        float distanceToClosestSystem = 0;
        int attemptCounter = 0;

        if(isInhabited)
            while(distanceToClosestSystem < distanceInhabitedMin){
                distanceToClosestSystem = 100000;
                int randomRange = ThreadLocalRandom.current().nextInt(radiusMin, radiusMax);
                float angle = ThreadLocalRandom.current().nextFloat() * 360;
                float x = (float)Math.cos(angle) * randomRange - bonusX;
                float y = (float)Math.sin(angle) * randomRange - bunusY;
                system.getLocation().set(x,y);
                for(StarSystemAPI system2: Global.getSector().getStarSystems()){
                    if(system.getName().equalsIgnoreCase(system2.getName()))
                        continue;
                    float distance = DistanceBetweenSystems(system,system2);
                    if(distanceToClosestSystem>distance)
                        distanceToClosestSystem = distance;
                }
                attemptCounter++;
                if(attemptCounter>10)
                    break;
            }
        else
            while(distanceToClosestSystem < distanceUninhabitedMin){
                distanceToClosestSystem = 100000;
                int randomRange = ThreadLocalRandom.current().nextInt(radiusMin, radiusMax);
                float angle = ThreadLocalRandom.current().nextFloat() * 360;
                float x = (float)Math.cos(angle) * randomRange - bonusX;
                float y = (float)Math.sin(angle) * randomRange - bunusY;
                system.getLocation().set(x,y);
                for(StarSystemAPI system2: Global.getSector().getStarSystems()){
                    if(system.getName().equalsIgnoreCase(system2.getName()))
                        continue;
                    float distance = DistanceBetweenSystems(system,system2);
                    if(distanceToClosestSystem>distance)
                        distanceToClosestSystem = distance;
                }
                attemptCounter++;
                if(attemptCounter>10)
                    break;
            }

        cleanup(system);
    }

    public float DistanceBetweenSystems(StarSystemAPI s1, StarSystemAPI s2){
        float x = s1.getLocation().getX() - s2.getLocation().getX();
        float y = s1.getLocation().getY() - s2.getLocation().getY();
        return (float)Math.sqrt(x*x+y*y);
    }

    public StarSystemAPI GetClosestSystem(StarSystemAPI system){
        StarSystemAPI closestSystem = null;
        float distanceToClosestSystem = 100000;
        for(StarSystemAPI system2: Global.getSector().getStarSystems()){
            if(system.getName().equalsIgnoreCase(system2.getName()))
                continue;
            float distance = DistanceBetweenSystems(system,system2);
            if(distanceToClosestSystem>distance){
                distanceToClosestSystem = distance;
                closestSystem = system2;
            }
        }
        return closestSystem;
    }
}
