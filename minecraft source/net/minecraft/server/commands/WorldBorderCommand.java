/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.arguments.DoubleArgumentType;
/*     */ import com.mojang.brigadier.arguments.FloatArgumentType;
/*     */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.TimeArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec2Argument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WorldBorderCommand
/*     */ {
/*  30 */   private static final SimpleCommandExceptionType ERROR_SAME_CENTER = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.center.failed"));
/*  31 */   private static final SimpleCommandExceptionType ERROR_SAME_SIZE = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.set.failed.nochange"));
/*  32 */   private static final SimpleCommandExceptionType ERROR_TOO_SMALL = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.set.failed.small"));
/*  33 */   private static final SimpleCommandExceptionType ERROR_TOO_BIG = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.set.failed.big", new Object[] { Double.valueOf(5.9999968E7D) }));
/*  34 */   private static final SimpleCommandExceptionType ERROR_TOO_FAR_OUT = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.set.failed.far", new Object[] { Double.valueOf(2.9999984E7D) }));
/*  35 */   private static final SimpleCommandExceptionType ERROR_SAME_WARNING_TIME = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.warning.time.failed"));
/*  36 */   private static final SimpleCommandExceptionType ERROR_SAME_WARNING_DISTANCE = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.warning.distance.failed"));
/*  37 */   private static final SimpleCommandExceptionType ERROR_SAME_DAMAGE_BUFFER = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.damage.buffer.failed"));
/*  38 */   private static final SimpleCommandExceptionType ERROR_SAME_DAMAGE_AMOUNT = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.damage.amount.failed"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  41 */     dispatcher.register(
/*  42 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("worldborder")
/*  43 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  44 */         .then(
/*  45 */           Commands.literal("add")
/*  46 */           .then((
/*  47 */             (RequiredArgumentBuilder)Commands.argument("distance", DoubleArgumentType.doubleArg(-5.9999968E7D, 5.9999968E7D))
/*  48 */             .executes(c -> setSize((CommandSourceStack)c.getSource(), ((CommandSourceStack)c.getSource()).getLevel().getWorldBorder().getSize() + DoubleArgumentType.getDouble(c, "distance"), 0L)))
/*  49 */             .then(
/*  50 */               Commands.argument("time", TimeArgument.time(0))
/*  51 */               .executes(c -> setSize((CommandSourceStack)c.getSource(), ((CommandSourceStack)c.getSource()).getLevel().getWorldBorder().getSize() + DoubleArgumentType.getDouble(c, "distance"), ((CommandSourceStack)c.getSource()).getLevel().getWorldBorder().getLerpTime() + IntegerArgumentType.getInteger(c, "time")))))))
/*     */ 
/*     */ 
/*     */         
/*  55 */         .then(
/*  56 */           Commands.literal("set")
/*  57 */           .then((
/*  58 */             (RequiredArgumentBuilder)Commands.argument("distance", DoubleArgumentType.doubleArg(-5.9999968E7D, 5.9999968E7D))
/*  59 */             .executes(c -> setSize((CommandSourceStack)c.getSource(), DoubleArgumentType.getDouble(c, "distance"), 0L)))
/*  60 */             .then(
/*  61 */               Commands.argument("time", TimeArgument.time(0))
/*  62 */               .executes(c -> setSize((CommandSourceStack)c.getSource(), DoubleArgumentType.getDouble(c, "distance"), IntegerArgumentType.getInteger(c, "time")))))))
/*     */ 
/*     */ 
/*     */         
/*  66 */         .then(
/*  67 */           Commands.literal("center")
/*  68 */           .then(
/*  69 */             Commands.argument("pos", Vec2Argument.vec2())
/*  70 */             .executes(c -> setCenter((CommandSourceStack)c.getSource(), Vec2Argument.getVec2(c, "pos"))))))
/*     */ 
/*     */         
/*  73 */         .then((
/*  74 */           (LiteralArgumentBuilder)Commands.literal("damage")
/*  75 */           .then(
/*  76 */             Commands.literal("amount")
/*  77 */             .then(
/*  78 */               Commands.argument("damagePerBlock", FloatArgumentType.floatArg(0.0F))
/*  79 */               .executes(c -> setDamageAmount((CommandSourceStack)c.getSource(), FloatArgumentType.getFloat(c, "damagePerBlock"))))))
/*     */ 
/*     */           
/*  82 */           .then(
/*  83 */             Commands.literal("buffer")
/*  84 */             .then(
/*  85 */               Commands.argument("distance", FloatArgumentType.floatArg(0.0F))
/*  86 */               .executes(c -> setDamageBuffer((CommandSourceStack)c.getSource(), FloatArgumentType.getFloat(c, "distance")))))))
/*     */ 
/*     */ 
/*     */         
/*  90 */         .then(
/*  91 */           Commands.literal("get")
/*  92 */           .executes(c -> getSize((CommandSourceStack)c.getSource()))))
/*     */         
/*  94 */         .then((
/*  95 */           (LiteralArgumentBuilder)Commands.literal("warning")
/*  96 */           .then(
/*  97 */             Commands.literal("distance")
/*  98 */             .then(
/*  99 */               Commands.argument("distance", IntegerArgumentType.integer(0))
/* 100 */               .executes(c -> setWarningDistance((CommandSourceStack)c.getSource(), IntegerArgumentType.getInteger(c, "distance"))))))
/*     */ 
/*     */           
/* 103 */           .then(
/* 104 */             Commands.literal("time")
/* 105 */             .then(
/* 106 */               Commands.argument("time", TimeArgument.time(0))
/* 107 */               .executes(c -> setWarningTime((CommandSourceStack)c.getSource(), IntegerArgumentType.getInteger(c, "time")))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int setDamageBuffer(CommandSourceStack source, float distance) throws CommandSyntaxException {
/* 115 */     WorldBorder border = source.getLevel().getWorldBorder();
/* 116 */     if (border.getSafeZone() == distance) {
/* 117 */       throw ERROR_SAME_DAMAGE_BUFFER.create();
/*     */     }
/* 119 */     border.setSafeZone(distance);
/* 120 */     source.sendSuccess(() -> Component.translatable("commands.worldborder.damage.buffer.success", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(distance) }) }), true);
/* 121 */     return (int)distance;
/*     */   }
/*     */   
/*     */   private static int setDamageAmount(CommandSourceStack source, float damagePerBlock) throws CommandSyntaxException {
/* 125 */     WorldBorder border = source.getLevel().getWorldBorder();
/* 126 */     if (border.getDamagePerBlock() == damagePerBlock) {
/* 127 */       throw ERROR_SAME_DAMAGE_AMOUNT.create();
/*     */     }
/* 129 */     border.setDamagePerBlock(damagePerBlock);
/* 130 */     source.sendSuccess(() -> Component.translatable("commands.worldborder.damage.amount.success", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(damagePerBlock) }) }), true);
/* 131 */     return (int)damagePerBlock;
/*     */   }
/*     */   
/*     */   private static int setWarningTime(CommandSourceStack source, int ticks) throws CommandSyntaxException {
/* 135 */     WorldBorder border = source.getLevel().getWorldBorder();
/* 136 */     if (border.getWarningTime() == ticks) {
/* 137 */       throw ERROR_SAME_WARNING_TIME.create();
/*     */     }
/* 139 */     border.setWarningTime(ticks);
/* 140 */     source.sendSuccess(() -> Component.translatable("commands.worldborder.warning.time.success", new Object[] { formatTicksToSeconds(ticks) }), true);
/* 141 */     return ticks;
/*     */   }
/*     */   
/*     */   private static int setWarningDistance(CommandSourceStack source, int distance) throws CommandSyntaxException {
/* 145 */     WorldBorder border = source.getLevel().getWorldBorder();
/* 146 */     if (border.getWarningBlocks() == distance) {
/* 147 */       throw ERROR_SAME_WARNING_DISTANCE.create();
/*     */     }
/* 149 */     border.setWarningBlocks(distance);
/* 150 */     source.sendSuccess(() -> Component.translatable("commands.worldborder.warning.distance.success", new Object[] { Integer.valueOf(distance) }), true);
/* 151 */     return distance;
/*     */   }
/*     */   
/*     */   private static int getSize(CommandSourceStack source) {
/* 155 */     double size = source.getLevel().getWorldBorder().getSize();
/* 156 */     source.sendSuccess(() -> Component.translatable("commands.worldborder.get", new Object[] { String.format(Locale.ROOT, "%.0f", new Object[] { Double.valueOf(size) }) }), false);
/* 157 */     return Mth.floor(size + 0.5D);
/*     */   }
/*     */   
/*     */   private static int setCenter(CommandSourceStack source, Vec2 center) throws CommandSyntaxException {
/* 161 */     WorldBorder border = source.getLevel().getWorldBorder();
/* 162 */     if (border.getCenterX() == center.x && border.getCenterZ() == center.y) {
/* 163 */       throw ERROR_SAME_CENTER.create();
/*     */     }
/*     */     
/* 166 */     if (Math.abs(center.x) > 2.9999984E7D || Math.abs(center.y) > 2.9999984E7D) {
/* 167 */       throw ERROR_TOO_FAR_OUT.create();
/*     */     }
/*     */     
/* 170 */     border.setCenter(center.x, center.y);
/* 171 */     source.sendSuccess(() -> Component.translatable("commands.worldborder.center.success", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(center.x) }), String.format(Locale.ROOT, "%.2f", new Object[] { Float.valueOf(center.y) }) }), true);
/*     */     
/* 173 */     return 0;
/*     */   }
/*     */   
/*     */   private static int setSize(CommandSourceStack source, double distance, long ticks) throws CommandSyntaxException {
/* 177 */     ServerLevel level = source.getLevel();
/* 178 */     WorldBorder border = level.getWorldBorder();
/* 179 */     double current = border.getSize();
/*     */     
/* 181 */     if (current == distance) {
/* 182 */       throw ERROR_SAME_SIZE.create();
/*     */     }
/* 184 */     if (distance < 1.0D) {
/* 185 */       throw ERROR_TOO_SMALL.create();
/*     */     }
/* 187 */     if (distance > 5.9999968E7D) {
/* 188 */       throw ERROR_TOO_BIG.create();
/*     */     }
/*     */     
/* 191 */     String formattedDistance = String.format(Locale.ROOT, "%.1f", new Object[] { Double.valueOf(distance) });
/* 192 */     if (ticks > 0L) {
/* 193 */       border.lerpSizeBetween(current, distance, ticks, level.getGameTime());
/* 194 */       if (distance > current) {
/* 195 */         source.sendSuccess(() -> Component.translatable("commands.worldborder.set.grow", new Object[] { formattedDistance, formatTicksToSeconds(ticks) }), true);
/*     */       } else {
/* 197 */         source.sendSuccess(() -> Component.translatable("commands.worldborder.set.shrink", new Object[] { formattedDistance, formatTicksToSeconds(ticks) }), true);
/*     */       } 
/*     */     } else {
/* 200 */       border.setSize(distance);
/* 201 */       source.sendSuccess(() -> Component.translatable("commands.worldborder.set.immediate", new Object[] { formattedDistance }), true);
/*     */     } 
/*     */     
/* 204 */     return (int)(distance - current);
/*     */   }
/*     */ 
/*     */   
/* 208 */   private static String formatTicksToSeconds(long ticks) { return String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(ticks / 20.0D) }); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\WorldBorderCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */