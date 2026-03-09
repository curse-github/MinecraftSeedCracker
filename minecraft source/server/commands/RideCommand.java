/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ 
/*    */ public class RideCommand {
/* 20 */   private static final DynamicCommandExceptionType ERROR_NOT_RIDING = new DynamicCommandExceptionType(entity -> Component.translatableEscape("commands.ride.not_riding", new Object[] { entity }));
/* 21 */   private static final Dynamic2CommandExceptionType ERROR_ALREADY_RIDING = new Dynamic2CommandExceptionType((entity, vehicle) -> Component.translatableEscape("commands.ride.already_riding", new Object[] { entity, vehicle }));
/* 22 */   private static final Dynamic2CommandExceptionType ERROR_MOUNT_FAILED = new Dynamic2CommandExceptionType((entity, vehicle) -> Component.translatableEscape("commands.ride.mount.failure.generic", new Object[] { entity, vehicle }));
/* 23 */   private static final SimpleCommandExceptionType ERROR_MOUNTING_PLAYER = new SimpleCommandExceptionType(Component.translatable("commands.ride.mount.failure.cant_ride_players"));
/* 24 */   private static final SimpleCommandExceptionType ERROR_MOUNTING_LOOP = new SimpleCommandExceptionType(Component.translatable("commands.ride.mount.failure.loop"));
/* 25 */   private static final SimpleCommandExceptionType ERROR_WRONG_DIMENSION = new SimpleCommandExceptionType(Component.translatable("commands.ride.mount.failure.wrong_dimension"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/* 28 */     dispatcher.register(
/* 29 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("ride")
/* 30 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 31 */         .then((
/* 32 */           (RequiredArgumentBuilder)Commands.argument("target", EntityArgument.entity())
/* 33 */           .then(
/* 34 */             Commands.literal("mount")
/* 35 */             .then(
/* 36 */               Commands.argument("vehicle", EntityArgument.entity())
/* 37 */               .executes(c -> mount((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"), EntityArgument.getEntity(c, "vehicle"))))))
/*    */ 
/*    */           
/* 40 */           .then(
/* 41 */             Commands.literal("dismount")
/* 42 */             .executes(c -> dismount((CommandSourceStack)c.getSource(), EntityArgument.getEntity(c, "target"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int mount(CommandSourceStack source, Entity target, Entity vehicle) throws CommandSyntaxException {
/* 49 */     Entity currentVehicle = target.getVehicle();
/* 50 */     if (currentVehicle != null) {
/* 51 */       throw ERROR_ALREADY_RIDING.create(target.getDisplayName(), currentVehicle.getDisplayName());
/*    */     }
/* 53 */     if (vehicle.getType() == EntityType.PLAYER) {
/* 54 */       throw ERROR_MOUNTING_PLAYER.create();
/*    */     }
/* 56 */     if (target.getSelfAndPassengers().anyMatch(e -> (e == vehicle))) {
/* 57 */       throw ERROR_MOUNTING_LOOP.create();
/*    */     }
/* 59 */     if (target.level() != vehicle.level()) {
/* 60 */       throw ERROR_WRONG_DIMENSION.create();
/*    */     }
/* 62 */     if (!target.startRiding(vehicle, true, true)) {
/* 63 */       throw ERROR_MOUNT_FAILED.create(target.getDisplayName(), vehicle.getDisplayName());
/*    */     }
/* 65 */     source.sendSuccess(() -> Component.translatable("commands.ride.mount.success", new Object[] { target.getDisplayName(), vehicle.getDisplayName() }), true);
/* 66 */     return 1;
/*    */   }
/*    */   
/*    */   private static int dismount(CommandSourceStack source, Entity target) throws CommandSyntaxException {
/* 70 */     Entity vehicle = target.getVehicle();
/* 71 */     if (vehicle == null) {
/* 72 */       throw ERROR_NOT_RIDING.create(target.getDisplayName());
/*    */     }
/*    */     
/* 75 */     target.stopRiding();
/* 76 */     source.sendSuccess(() -> Component.translatable("commands.ride.dismount.success", new Object[] { target.getDisplayName(), vehicle.getDisplayName() }), true);
/* 77 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\RideCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */