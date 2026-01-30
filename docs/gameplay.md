# Gameplay Guide

Building and managing a Phoenix's Fission reactor requires careful planning of heat generation and cooling.

## Reactor Basics
A reactor consists of a multiblock structure containing several internal components:

1.  **Fuel Rods**: The heart of the reactor. They generate heat and produce power (or steam).
2.  **Moderators**: Placed near fuel rods to increase efficiency or provide bonuses like parallel recipe processing.
3.  **Coolers**: Essential for removing heat. Some require a constant supply of coolant fluid.

## Heat & Meltdown
Every reactor has a `Max Safe Heat` limit. If the heat exceeds this:
- A **Meltdown Timer** starts.
- You have a grace period (configurable, usually 60s) to cool the reactor down.
- If the timer hits zero, the reactor **explodes**.

!!! danger "Warning"
    If the heat reaches the **Hard Heat Clamp**, the reactor will explode instantly!

## Efficiency
Continuous running builds up a **Burn Bonus**, which increases power output but also increases heat generation.
