package net.phoenix.core.integration.jade.provider;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import net.phoenix.core.PhoenixFission;
import net.phoenix.core.common.machine.multiblock.BreederWorkableElectricMultiblockMachine;
import net.phoenix.core.common.machine.multiblock.FissionWorkableElectricMultiblockMachine;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class FissionMachineProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    public static final ResourceLocation UID = PhoenixFission.id("fission_machine_info");

    // Tag keys (keep consistent)
    private static final String NBT_HEAT = "pf_heat";
    private static final String NBT_NET_HEAT = "pf_net_heat";
    private static final String NBT_MELTDOWN_SECONDS = "pf_meltdown_seconds";
    private static final String NBT_HAS_COOLANT = "pf_has_coolant";
    private static final String NBT_IS_BREEDER = "pf_is_breeder";
    private static final String NBT_BREEDING_PRODUCT = "pf_breeding_product";
    private static final String NBT_RUNNING = "pf_running";
    private static final String NBT_PARALLELS = "pf_parallels";
    private static final String NBT_EUT = "pf_eut";
    private static final String NBT_RODS = "pf_rods";
    private static final String NBT_COOLERS = "pf_coolers";
    private static final String NBT_MODS = "pf_mods";
    private static final String NBT_GATE_FAIL = "pf_gate_fail"; // optional: reason


    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        // Respect Jade toggle
        if (!config.get(UID)) return;

        if (!(accessor.getBlockEntity() instanceof MetaMachineBlockEntity metaBE &&
                metaBE.getMetaMachine() instanceof FissionWorkableElectricMultiblockMachine)) {
            return;
        }

        CompoundTag data = accessor.getServerData();
        if (data == null || data.isEmpty()) return;

        // Heat + net heat
        double heat = data.getDouble(NBT_HEAT);
        double netHeat = data.getDouble(NBT_NET_HEAT);

        tooltip.add(Component.translatable("jade.phoenix_fission.heat", (long) heat));

        // Optional: show net heat if you want (you already have a lang key for net heat on machines,
        // but not for jade; easiest is a literal line)
        // tooltip.add(Component.literal(String.format("Net Heat: %.2f HU/t", netHeat)));

        // Meltdown
        int meltdownSeconds = data.getInt(NBT_MELTDOWN_SECONDS);
        if (meltdownSeconds > 0) {
            tooltip.add(Component.translatable("jade.phoenix_fission.fission_meltdown_timer", meltdownSeconds)
                    .withStyle(s -> s.withColor(0xFFAA00)));
        } else {
            tooltip.add(Component.translatable("jade.phoenix_fission.fission_safe")
                    .withStyle(s -> s.withColor(0x33FF33)));
        }

        // Alerts
        if (!data.getBoolean(NBT_HAS_COOLANT)) {
            tooltip.add(Component.translatable("jade.phoenix_fission.fission_no_coolant")
                    .withStyle(s -> s.withColor(0xFF3333)));
        }

        if (netHeat > 0.0001) {
            tooltip.add(Component.translatable("jade.phoenix_fission.fission_heating")
                    .withStyle(s -> s.withColor(0xFF5555)));
        }
        boolean running = data.getBoolean(NBT_RUNNING);
        int parallels = data.getInt(NBT_PARALLELS);
        long eut = data.getLong(NBT_EUT);

        tooltip.add(Component.literal("Running: " + running)
                .withStyle(s -> s.withColor(running ? 0x33FF33 : 0xFF3333)));

        tooltip.add(Component.literal("Parallels: " + parallels));
        tooltip.add(Component.literal("EU/t: " + eut));

        int rods = data.getInt(NBT_RODS);
        int coolers = data.getInt(NBT_COOLERS);
        int mods = data.getInt(NBT_MODS);
        tooltip.add(Component.literal("Rods: " + rods + "  Mods: " + mods + "  Coolers: " + coolers));


        // Breeder info
        if (data.getBoolean(NBT_IS_BREEDER) && data.contains(NBT_BREEDING_PRODUCT)) {
            tooltip.add(Component.translatable(
                            "phoenix.fission.blanket_output",
                            Component.literal(data.getString(NBT_BREEDING_PRODUCT)))
                    .withStyle(s -> s.withColor(0xFFBBFF)));
        }
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        if (!(accessor.getBlockEntity() instanceof MetaMachineBlockEntity metaBE &&
                metaBE.getMetaMachine() instanceof FissionWorkableElectricMultiblockMachine machine)) {
            return;
        }

        // counts
        tag.putInt(NBT_RODS, machine.getActiveFuelRods().size());
        tag.putInt(NBT_COOLERS, machine.getActiveCoolers().size());
        tag.putInt(NBT_MODS, machine.getActiveModerators().size());

        // heat + net heat
        tag.putDouble(NBT_HEAT, machine.getHeat());
        tag.putDouble(NBT_NET_HEAT, machine.getNetHeatPerTick());

        // coolant + running
        tag.putBoolean(NBT_HAS_COOLANT, machine.lastHasCoolant);
        tag.putBoolean(NBT_RUNNING, machine.wasRunningLastTick());

        // parallels + EU/t snapshot
        tag.putInt(NBT_PARALLELS, machine.lastParallels);
        tag.putLong(NBT_EUT, machine.lastGeneratedEUt);

        // meltdown seconds remaining
        tag.putInt(NBT_MELTDOWN_SECONDS, machine.getMeltdownSecondsRemaining());

        // breeder info
        boolean breeder = machine instanceof BreederWorkableElectricMultiblockMachine;
        tag.putBoolean(NBT_IS_BREEDER, breeder);

        if (breeder) {
            BreederWorkableElectricMultiblockMachine b = (BreederWorkableElectricMultiblockMachine) machine;
            if (b.getPrimaryBlanket() != null) {
                // show output key or a nicer name if you have one
                tag.putString(NBT_BREEDING_PRODUCT, b.getPrimaryBlanket().getOutputKey());
            }
        }

        // optional: gate fail / status reason (only if you have a field for it)
        // tag.putString(NBT_GATE_FAIL, machine.getLastGateFailReason());
    }


    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
