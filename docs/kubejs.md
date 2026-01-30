# KubeJS Integration

Phoenix's Fission provides a dedicated KubeJS plugin to add new reactor components easily.

## Adding Components
You can register new Fuel Rods, Coolers, Moderators, and Blankets in your `startup_scripts`.

### Example: Custom Fuel Rod
```javascript
PhoenixJS.registerFuelRod("super_uranium", (builder) => {
    builder.name("Super Uranium Fuel Rod")
           .heatPerTick(500)
           .efficiency(1.5)
           .fuelItem("minecraft:nether_star") // Example fuel
});
```

### Example: Custom Cooler
```javascript
PhoenixJS.registerCooler("liquid_nitrogen", (builder) => {
    builder.name("Nitrogen Cooler")
           .coolingAmount(1000)
           .requiresCoolant(true)
           .coolantFluid("gtceu:liquid_nitrogen")
});
```

## Available Builders
- `PhoenixJS.registerFuelRod`
- `PhoenixJS.registerCooler`
- `PhoenixJS.registerModerator`
- `PhoenixJS.registerBlanket`
