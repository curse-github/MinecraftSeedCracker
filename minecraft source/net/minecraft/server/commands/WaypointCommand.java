/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.HexFormat;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ColorArgument;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.HexColorArgument;
/*     */ import net.minecraft.commands.arguments.IdentifierArgument;
/*     */ import net.minecraft.commands.arguments.WaypointArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.chat.ClickEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.HoverEvent;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.waypoints.Waypoint;
/*     */ import net.minecraft.world.waypoints.WaypointStyleAsset;
/*     */ import net.minecraft.world.waypoints.WaypointStyleAssets;
/*     */ import net.minecraft.world.waypoints.WaypointTransmitter;
/*     */ 
/*     */ 
/*     */ public class WaypointCommand
/*     */ {
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
/*  42 */     dispatcher.register(
/*  43 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("waypoint")
/*  44 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  45 */         .then(
/*  46 */           Commands.literal("list")
/*  47 */           .executes(c -> listWaypoints((CommandSourceStack)c.getSource()))))
/*     */         
/*  49 */         .then(
/*  50 */           Commands.literal("modify")
/*  51 */           .then((
/*  52 */             (RequiredArgumentBuilder)Commands.argument("waypoint", EntityArgument.entity())
/*  53 */             .then((
/*  54 */               (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("color")
/*  55 */               .then(
/*  56 */                 Commands.argument("color", ColorArgument.color())
/*  57 */                 .executes(c -> setWaypointColor((CommandSourceStack)c.getSource(), WaypointArgument.getWaypoint(c, "waypoint"), ColorArgument.getColor(c, "color")))))
/*     */               
/*  59 */               .then(
/*  60 */                 Commands.literal("hex").then(
/*  61 */                   Commands.argument("color", HexColorArgument.hexColor())
/*  62 */                   .executes(c -> setWaypointColor((CommandSourceStack)c.getSource(), WaypointArgument.getWaypoint(c, "waypoint"), HexColorArgument.getHexColor(c, "color"))))))
/*     */ 
/*     */               
/*  65 */               .then(
/*  66 */                 Commands.literal("reset")
/*  67 */                 .executes(c -> resetWaypointColor((CommandSourceStack)c.getSource(), WaypointArgument.getWaypoint(c, "waypoint"))))))
/*     */ 
/*     */             
/*  70 */             .then((
/*  71 */               (LiteralArgumentBuilder)Commands.literal("style")
/*  72 */               .then(
/*  73 */                 Commands.literal("reset")
/*  74 */                 .executes(c -> setWaypointStyle((CommandSourceStack)c.getSource(), WaypointArgument.getWaypoint(c, "waypoint"), WaypointStyleAssets.DEFAULT))))
/*     */               
/*  76 */               .then(
/*  77 */                 Commands.literal("set")
/*  78 */                 .then(
/*  79 */                   Commands.argument("style", IdentifierArgument.id())
/*  80 */                   .executes(c -> setWaypointStyle((CommandSourceStack)c.getSource(), WaypointArgument.getWaypoint(c, "waypoint"), ResourceKey.create(WaypointStyleAssets.ROOT_ID, IdentifierArgument.getId(c, "style"))))))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int setWaypointStyle(CommandSourceStack source, WaypointTransmitter waypoint, ResourceKey<WaypointStyleAsset> style) {
/*  90 */     mutateIcon(source, waypoint, icon -> icon.style = style);
/*  91 */     source.sendSuccess(() -> Component.translatable("commands.waypoint.modify.style"), false);
/*  92 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setWaypointColor(CommandSourceStack source, WaypointTransmitter waypoint, ChatFormatting color) {
/*  96 */     mutateIcon(source, waypoint, icon -> icon.color = Optional.of(color.getColor()));
/*  97 */     source.sendSuccess(() -> Component.translatable("commands.waypoint.modify.color", new Object[] { Component.literal(color.getName()).withStyle(color) }), false);
/*  98 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setWaypointColor(CommandSourceStack source, WaypointTransmitter waypoint, Integer color) {
/* 102 */     mutateIcon(source, waypoint, icon -> icon.color = Optional.of(color));
/* 103 */     source.sendSuccess(() -> Component.translatable("commands.waypoint.modify.color", new Object[] { Component.literal(HexFormat.of().withUpperCase().toHexDigits(ARGB.color(0, color.intValue()), 6)).withColor(color.intValue()) }), false);
/* 104 */     return 0;
/*     */   }
/*     */   
/*     */   private static int resetWaypointColor(CommandSourceStack source, WaypointTransmitter waypoint) {
/* 108 */     mutateIcon(source, waypoint, icon -> icon.color = Optional.empty());
/* 109 */     source.sendSuccess(() -> Component.translatable("commands.waypoint.modify.color.reset"), false);
/* 110 */     return 0;
/*     */   }
/*     */   
/*     */   private static int listWaypoints(CommandSourceStack source) {
/* 114 */     ServerLevel level = source.getLevel();
/* 115 */     Set<WaypointTransmitter> waypoints = level.getWaypointManager().transmitters();
/* 116 */     String dimension = level.dimension().identifier().toString();
/*     */     
/* 118 */     if (waypoints.isEmpty()) {
/* 119 */       source.sendSuccess(() -> Component.translatable("commands.waypoint.list.empty", new Object[] { dimension }), false);
/* 120 */       return 0;
/*     */     } 
/*     */     
/* 123 */     Component waypointNames = ComponentUtils.formatList(waypoints.stream().map(transmitter -> {
/* 124 */             if (transmitter instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)transmitter;
/* 125 */               BlockPos pos = livingEntity.blockPosition();
/* 126 */               return livingEntity.getFeedbackDisplayName().copy().withStyle(()); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 132 */             return Component.literal(transmitter.toString());
/*     */           
/* 134 */           }).toList(), Function.identity());
/*     */     
/* 136 */     source.sendSuccess(() -> Component.translatable("commands.waypoint.list.success", new Object[] { Integer.valueOf(waypoints.size()), dimension, waypointNames }), false);
/*     */     
/* 138 */     return waypoints.size();
/*     */   }
/*     */   
/*     */   private static void mutateIcon(CommandSourceStack source, WaypointTransmitter waypoint, Consumer<Waypoint.Icon> iconConsumer) {
/* 142 */     ServerLevel level = source.getLevel();
/* 143 */     level.getWaypointManager().untrackWaypoint(waypoint);
/*     */     
/* 145 */     iconConsumer.accept(waypoint.waypointIcon());
/*     */     
/* 147 */     level.getWaypointManager().trackWaypoint(waypoint);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\WaypointCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */