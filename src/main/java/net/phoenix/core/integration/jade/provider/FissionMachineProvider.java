package net.phoenix.core.integration.jade.provider;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
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
    private static final String NBT_BLANKETS = "pf_blankets";
    private static final String NBT_COOLING_POWER = "pf_cooling_power";
    private static final String NBT_GATE_FAIL = "pf_gate_fail";
    private static final String NBT_BLANKET_INPUT = "pf_blanket_input";
    private static final String NBT_BLANKET_OUTPUT = "pf_blanket_output";
    private static final String NBT_BLANKET_AMOUNT = "pf_blanket_amount";

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (!config.get(UID)) return;

        if (!(accessor.getBlockEntity() instanceof MetaMachineBlockEntity metaBE &&
                metaBE.getMetaMachine() instanceof FissionWorkableElectricMultiblockMachine)) {
            return;
        }

        CompoundTag data = accessor.getServerData();
        if (data == null || data.isEmpty()) return;

        double heat = data.getDouble(NBT_HEAT);
        double netHeat = data.getDouble(NBT_NET_HEAT);

        tooltip.add(Component.translatable("jade.phoenix_fission.heat", (long) heat));

        int meltdownSeconds = data.getInt(NBT_MELTDOWN_SECONDS);
        if (meltdownSeconds > 0) {
            tooltip.add(Component.translatable("jade.phoenix_fission.fission_meltdown_timer", meltdownSeconds)
                    .withStyle(s -> s.withColor(0xFFAA00)));
        } else {
            tooltip.add(Component.translatable("jade.phoenix_fission.fission_safe")
                    .withStyle(s -> s.withColor(0x33FF33)));
        }

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

        int coolingPower = data.getInt(NBT_COOLING_POWER);
        tooltip.add(Component.literal("Max Cooling Power: " + coolingPower + " HU/t")
                .withStyle(s -> s.withColor(0x55FFFF)));

        if (data.getBoolean(NBT_IS_BREEDER) && data.contains(NBT_BLANKET_OUTPUT)) {
            String inKey = data.getString(NBT_BLANKET_INPUT);
            String outKey = data.getString(NBT_BLANKET_OUTPUT);

            Component inName = resolveKeyToDisplayName(inKey);
            Component outName = resolveKeyToDisplayName(outKey);

            tooltip.add(Component.translatable("jade.phoenix_fission.blanket_input", inName)
                    .withStyle(s -> s.withColor(0xAAAAFF)));

            tooltip.add(Component.translatable("jade.phoenix_fission.blanket_output", outName)
                    .withStyle(s -> s.withColor(0xFFBBFF)));

            if (data.contains(NBT_BLANKET_AMOUNT)) {
                int amt = data.getInt(NBT_BLANKET_AMOUNT);
                tooltip.add(Component.translatable("jade.phoenix_fission.blanket_amount", amt)
                        .withStyle(s -> s.withColor(0xBBBBBB)));
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        if (!(accessor.getBlockEntity() instanceof MetaMachineBlockEntity metaBE &&
                metaBE.getMetaMachine() instanceof FissionWorkableElectricMultiblockMachine machine)) {
            return;
        }

        tag.putInt(NBT_RODS, machine.getActiveFuelRods().size());
        tag.putInt(NBT_COOLERS, machine.getActiveCoolers().size());
        tag.putInt(NBT_MODS, machine.getActiveModerators().size());
        tag.putInt(NBT_BLANKETS, machine.getActiveBlankets().size());

        tag.putDouble(NBT_HEAT, machine.getHeat());
        tag.putDouble(NBT_NET_HEAT, machine.getNetHeatPerTick());

        tag.putBoolean(NBT_HAS_COOLANT, machine.lastHasCoolant);
        tag.putBoolean(NBT_RUNNING, machine.wasRunningLastTick());

        tag.putInt(NBT_PARALLELS, machine.lastParallels);
        tag.putLong(NBT_EUT, machine.lastGeneratedEUt);

        tag.putInt(NBT_COOLING_POWER, machine.lastProvidedCooling);

        tag.putInt(NBT_MELTDOWN_SECONDS, machine.getMeltdownSecondsRemaining());

        boolean breeder = machine instanceof BreederWorkableElectricMultiblockMachine;
        tag.putBoolean(NBT_IS_BREEDER, breeder);
        if (breeder) {
            BreederWorkableElectricMultiblockMachine b = (BreederWorkableElectricMultiblockMachine) machine;
            if (b.getPrimaryBlanket() != null) {
                tag.putString(NBT_BLANKET_INPUT, b.getPrimaryBlanket().getInputKey());
                tag.putString(NBT_BLANKET_OUTPUT, b.getPrimaryBlanket().getOutputKey());

                tag.putInt(NBT_BLANKET_AMOUNT, Math.max(0, b.getPrimaryBlanket().getAmountPerCycle()));
            }
        }
    }

    private static Component resolveKeyToDisplayName(String key) {
        if (key == null || key.isEmpty() || "none".equalsIgnoreCase(key)) {
            return Component.literal("None");
        }

        Material mat = GTMaterials.get(key);
        if (mat != null && mat != GTMaterials.NULL) {
            try {

                String transKey = mat.getDefaultTranslation();
                if (transKey != null && !transKey.isEmpty()) {
                    return Component.translatable(transKey);
                }
            } catch (Throwable ignored) {}

            return Component.literal(key);
        }

        ResourceLocation rl = ResourceLocation.tryParse(key);
        if (rl != null) {
            var item = ForgeRegistries.ITEMS.getValue(rl);
            if (item != null && item != net.minecraft.world.item.Items.AIR) {
                return new ItemStack(item, 1).getHoverName();
            }

            Fluid fluid = ForgeRegistries.FLUIDS.getValue(rl);
            if (fluid != null && fluid != Fluids.EMPTY) {
                return Component.translatable(fluid.getFluidType().getDescriptionId());
            }
        }

        return Component.literal(key);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
