package net.phoenix.core;

import net.phoenix.core.api.block.IFissionBlanketType;
import net.phoenix.core.api.block.IFissionCoolerType;
import net.phoenix.core.api.block.IFissionFuelRodType;
import net.phoenix.core.api.block.IFissionModeratorType;
import net.phoenix.core.common.block.FissionBlanketBlock;
import net.phoenix.core.common.block.FissionCoolerBlock;
import net.phoenix.core.common.block.FissionFuelRodBlock;
import net.phoenix.core.common.block.FissionModeratorBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PhoenixAPI {

    public static final Logger LOGGER = LogManager.getLogger("PhoenixCore");
    public static PhoenixAPI instance;

    public static final Map<IFissionCoolerType, Supplier<FissionCoolerBlock>> FISSION_COOLERS = new HashMap<>();
    public static final Map<IFissionModeratorType, Supplier<FissionModeratorBlock>> FISSION_MODERATORS = new HashMap<>();
    public static final Map<IFissionFuelRodType, Supplier<FissionFuelRodBlock>> FISSION_FUEL_RODS = new HashMap<>();
    public static final Map<IFissionBlanketType, Supplier<FissionBlanketBlock>> FISSION_BLANKETS = new HashMap<>();
}
