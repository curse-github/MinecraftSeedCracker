/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*     */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*     */ import net.minecraft.commands.arguments.EntityArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Coordinates;
/*     */ import net.minecraft.commands.arguments.coordinates.RotationArgument;
/*     */ import net.minecraft.commands.arguments.coordinates.Vec3Argument;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Relative;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TeleportCommand
/*     */ {
/*  45 */   private static final SimpleCommandExceptionType INVALID_POSITION = new SimpleCommandExceptionType(Component.translatable("commands.teleport.invalidPosition"));
/*     */   
/*     */   public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
/*  48 */     LiteralCommandNode<CommandSourceStack> teleport = dispatcher.register(
/*  49 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("teleport")
/*  50 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  51 */         .then(
/*  52 */           Commands.argument("location", Vec3Argument.vec3())
/*  53 */           .executes(c -> teleportToPos((CommandSourceStack)c.getSource(), Collections.singleton(((CommandSourceStack)c.getSource()).getEntityOrException()), ((CommandSourceStack)c.getSource()).getLevel(), Vec3Argument.getCoordinates(c, "location"), null, null))))
/*     */         
/*  55 */         .then(
/*  56 */           Commands.argument("destination", EntityArgument.entity())
/*  57 */           .executes(c -> teleportToEntity((CommandSourceStack)c.getSource(), Collections.singleton(((CommandSourceStack)c.getSource()).getEntityOrException()), EntityArgument.getEntity(c, "destination")))))
/*     */         
/*  59 */         .then((
/*  60 */           (RequiredArgumentBuilder)Commands.argument("targets", EntityArgument.entities())
/*  61 */           .then((
/*  62 */             (RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("location", Vec3Argument.vec3())
/*  63 */             .executes(c -> teleportToPos((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), ((CommandSourceStack)c.getSource()).getLevel(), Vec3Argument.getCoordinates(c, "location"), null, null)))
/*  64 */             .then(
/*  65 */               Commands.argument("rotation", RotationArgument.rotation())
/*  66 */               .executes(c -> teleportToPos((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), ((CommandSourceStack)c.getSource()).getLevel(), Vec3Argument.getCoordinates(c, "location"), RotationArgument.getRotation(c, "rotation"), null))))
/*     */             
/*  68 */             .then((
/*  69 */               (LiteralArgumentBuilder)Commands.literal("facing")
/*  70 */               .then(
/*  71 */                 Commands.literal("entity")
/*  72 */                 .then((
/*  73 */                   (RequiredArgumentBuilder)Commands.argument("facingEntity", EntityArgument.entity())
/*  74 */                   .executes(c -> teleportToPos((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), ((CommandSourceStack)c.getSource()).getLevel(), Vec3Argument.getCoordinates(c, "location"), null, new LookAt.LookAtEntity(EntityArgument.getEntity(c, "facingEntity"), EntityAnchorArgument.Anchor.FEET))))
/*  75 */                   .then(
/*  76 */                     Commands.argument("facingAnchor", EntityAnchorArgument.anchor())
/*  77 */                     .executes(c -> teleportToPos((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), ((CommandSourceStack)c.getSource()).getLevel(), Vec3Argument.getCoordinates(c, "location"), null, new LookAt.LookAtEntity(EntityArgument.getEntity(c, "facingEntity"), EntityAnchorArgument.getAnchor(c, "facingAnchor"))))))))
/*     */ 
/*     */ 
/*     */               
/*  81 */               .then(
/*  82 */                 Commands.argument("facingLocation", Vec3Argument.vec3())
/*  83 */                 .executes(c -> teleportToPos((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), ((CommandSourceStack)c.getSource()).getLevel(), Vec3Argument.getCoordinates(c, "location"), null, new LookAt.LookAtPosition(Vec3Argument.getVec3(c, "facingLocation"))))))))
/*     */ 
/*     */ 
/*     */           
/*  87 */           .then(
/*  88 */             Commands.argument("destination", EntityArgument.entity())
/*  89 */             .executes(c -> teleportToEntity((CommandSourceStack)c.getSource(), EntityArgument.getEntities(c, "targets"), EntityArgument.getEntity(c, "destination"))))));
/*     */ 
/*     */ 
/*     */     
/*  93 */     dispatcher.register(
/*  94 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("tp")
/*  95 */         .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/*  96 */         .redirect(teleport));
/*     */   }
/*     */ 
/*     */   
/*     */   private static int teleportToEntity(CommandSourceStack source, Collection<? extends Entity> entities, Entity destination) throws CommandSyntaxException {
/* 101 */     for (Entity entity : entities)
/*     */     {
/*     */       
/* 104 */       performTeleport(source, entity, (ServerLevel)destination.level(), destination.getX(), destination.getY(), destination.getZ(), EnumSet.noneOf(Relative.class), destination.getYRot(), destination.getXRot(), null);
/*     */     }
/*     */     
/* 107 */     if (entities.size() == 1) {
/* 108 */       source.sendSuccess(() -> Component.translatable("commands.teleport.success.entity.single", new Object[] { ((Entity)entities.iterator().next()).getDisplayName(), destination.getDisplayName() }), true);
/*     */     } else {
/* 110 */       source.sendSuccess(() -> Component.translatable("commands.teleport.success.entity.multiple", new Object[] { Integer.valueOf(entities.size()), destination.getDisplayName() }), true);
/*     */     } 
/*     */     
/* 113 */     return entities.size();
/*     */   }
/*     */   
/*     */   private static int teleportToPos(CommandSourceStack source, Collection<? extends Entity> entities, ServerLevel level, Coordinates destination, Coordinates rotation, LookAt lookAt) throws CommandSyntaxException {
/* 117 */     Vec3 pos = destination.getPosition(source);
/* 118 */     Vec2 rot = (rotation == null) ? null : rotation.getRotation(source);
/*     */     
/* 120 */     for (Entity entity : entities) {
/* 121 */       Set<Relative> relatives = getRelatives(destination, rotation, (entity.level().dimension() == level.dimension()));
/* 122 */       if (rot == null) {
/* 123 */         performTeleport(source, entity, level, pos.x, pos.y, pos.z, relatives, entity.getYRot(), entity.getXRot(), lookAt); continue;
/*     */       } 
/* 125 */       performTeleport(source, entity, level, pos.x, pos.y, pos.z, relatives, rot.y, rot.x, lookAt);
/*     */     } 
/*     */ 
/*     */     
/* 129 */     if (entities.size() == 1) {
/* 130 */       source.sendSuccess(() -> Component.translatable("commands.teleport.success.location.single", new Object[] { ((Entity)entities.iterator().next()).getDisplayName(), formatDouble(pos.x), formatDouble(pos.y), formatDouble(pos.z) }), true);
/*     */     } else {
/* 132 */       source.sendSuccess(() -> Component.translatable("commands.teleport.success.location.multiple", new Object[] { Integer.valueOf(entities.size()), formatDouble(pos.x), formatDouble(pos.y), formatDouble(pos.z) }), true);
/*     */     } 
/*     */     
/* 135 */     return entities.size();
/*     */   }
/*     */   
/*     */   private static Set<Relative> getRelatives(Coordinates destination, Coordinates rotation, boolean sameDimension) {
/* 139 */     Set<Relative> dir = Relative.direction(destination.isXRelative(), destination.isYRelative(), destination.isZRelative());
/* 140 */     Set<Relative> pos = sameDimension ? Relative.position(destination.isXRelative(), destination.isYRelative(), destination.isZRelative()) : Set.of();
/* 141 */     Set<Relative> rot = (rotation == null) ? Relative.ROTATION : Relative.rotation(rotation.isYRelative(), rotation.isXRelative());
/* 142 */     return Relative.union(new Set[] { dir, pos, rot });
/*     */   }
/*     */ 
/*     */   
/* 146 */   private static String formatDouble(double value) { return String.format(Locale.ROOT, "%f", new Object[] { Double.valueOf(value) }); }
/*     */   
/*     */   private static void performTeleport(CommandSourceStack source, Entity victim, ServerLevel level, double x, double y, double z, Set<Relative> relatives, float yRot, float xRot, LookAt lookAt) throws CommandSyntaxException { // Byte code:
/*     */     //   0: dload_3
/*     */     //   1: dload #5
/*     */     //   3: dload #7
/*     */     //   5: invokestatic containing : (DDD)Lnet/minecraft/core/BlockPos;
/*     */     //   8: astore #13
/*     */     //   10: aload #13
/*     */     //   12: invokestatic isInSpawnableBounds : (Lnet/minecraft/core/BlockPos;)Z
/*     */     //   15: ifne -> 25
/*     */     //   18: getstatic net/minecraft/server/commands/TeleportCommand.INVALID_POSITION : Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
/*     */     //   21: invokevirtual create : ()Lcom/mojang/brigadier/exceptions/CommandSyntaxException;
/*     */     //   24: athrow
/*     */     //   25: aload #9
/*     */     //   27: getstatic net/minecraft/world/entity/Relative.X : Lnet/minecraft/world/entity/Relative;
/*     */     //   30: invokeinterface contains : (Ljava/lang/Object;)Z
/*     */     //   35: ifeq -> 47
/*     */     //   38: dload_3
/*     */     //   39: aload_1
/*     */     //   40: invokevirtual getX : ()D
/*     */     //   43: dsub
/*     */     //   44: goto -> 48
/*     */     //   47: dload_3
/*     */     //   48: dstore #14
/*     */     //   50: aload #9
/*     */     //   52: getstatic net/minecraft/world/entity/Relative.Y : Lnet/minecraft/world/entity/Relative;
/*     */     //   55: invokeinterface contains : (Ljava/lang/Object;)Z
/*     */     //   60: ifeq -> 73
/*     */     //   63: dload #5
/*     */     //   65: aload_1
/*     */     //   66: invokevirtual getY : ()D
/*     */     //   69: dsub
/*     */     //   70: goto -> 75
/*     */     //   73: dload #5
/*     */     //   75: dstore #16
/*     */     //   77: aload #9
/*     */     //   79: getstatic net/minecraft/world/entity/Relative.Z : Lnet/minecraft/world/entity/Relative;
/*     */     //   82: invokeinterface contains : (Ljava/lang/Object;)Z
/*     */     //   87: ifeq -> 100
/*     */     //   90: dload #7
/*     */     //   92: aload_1
/*     */     //   93: invokevirtual getZ : ()D
/*     */     //   96: dsub
/*     */     //   97: goto -> 102
/*     */     //   100: dload #7
/*     */     //   102: dstore #18
/*     */     //   104: aload #9
/*     */     //   106: getstatic net/minecraft/world/entity/Relative.Y_ROT : Lnet/minecraft/world/entity/Relative;
/*     */     //   109: invokeinterface contains : (Ljava/lang/Object;)Z
/*     */     //   114: ifeq -> 127
/*     */     //   117: fload #10
/*     */     //   119: aload_1
/*     */     //   120: invokevirtual getYRot : ()F
/*     */     //   123: fsub
/*     */     //   124: goto -> 129
/*     */     //   127: fload #10
/*     */     //   129: fstore #20
/*     */     //   131: aload #9
/*     */     //   133: getstatic net/minecraft/world/entity/Relative.X_ROT : Lnet/minecraft/world/entity/Relative;
/*     */     //   136: invokeinterface contains : (Ljava/lang/Object;)Z
/*     */     //   141: ifeq -> 154
/*     */     //   144: fload #11
/*     */     //   146: aload_1
/*     */     //   147: invokevirtual getXRot : ()F
/*     */     //   150: fsub
/*     */     //   151: goto -> 156
/*     */     //   154: fload #11
/*     */     //   156: fstore #21
/*     */     //   158: fload #20
/*     */     //   160: invokestatic wrapDegrees : (F)F
/*     */     //   163: fstore #22
/*     */     //   165: fload #21
/*     */     //   167: invokestatic wrapDegrees : (F)F
/*     */     //   170: fstore #23
/*     */     //   172: aload_1
/*     */     //   173: aload_2
/*     */     //   174: dload #14
/*     */     //   176: dload #16
/*     */     //   178: dload #18
/*     */     //   180: aload #9
/*     */     //   182: fload #22
/*     */     //   184: fload #23
/*     */     //   186: iconst_1
/*     */     //   187: invokevirtual teleportTo : (Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FFZ)Z
/*     */     //   190: ifne -> 194
/*     */     //   193: return
/*     */     //   194: aload #12
/*     */     //   196: ifnull -> 208
/*     */     //   199: aload #12
/*     */     //   201: aload_0
/*     */     //   202: aload_1
/*     */     //   203: invokeinterface perform : (Lnet/minecraft/commands/CommandSourceStack;Lnet/minecraft/world/entity/Entity;)V
/*     */     //   208: aload_1
/*     */     //   209: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   212: ifeq -> 229
/*     */     //   215: aload_1
/*     */     //   216: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   219: astore #24
/*     */     //   221: aload #24
/*     */     //   223: invokevirtual isFallFlying : ()Z
/*     */     //   226: ifne -> 248
/*     */     //   229: aload_1
/*     */     //   230: aload_1
/*     */     //   231: invokevirtual getDeltaMovement : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   234: dconst_1
/*     */     //   235: dconst_0
/*     */     //   236: dconst_1
/*     */     //   237: invokevirtual multiply : (DDD)Lnet/minecraft/world/phys/Vec3;
/*     */     //   240: invokevirtual setDeltaMovement : (Lnet/minecraft/world/phys/Vec3;)V
/*     */     //   243: aload_1
/*     */     //   244: iconst_1
/*     */     //   245: invokevirtual setOnGround : (Z)V
/*     */     //   248: aload_1
/*     */     //   249: instanceof net/minecraft/world/entity/PathfinderMob
/*     */     //   252: ifeq -> 269
/*     */     //   255: aload_1
/*     */     //   256: checkcast net/minecraft/world/entity/PathfinderMob
/*     */     //   259: astore #24
/*     */     //   261: aload #24
/*     */     //   263: invokevirtual getNavigation : ()Lnet/minecraft/world/entity/ai/navigation/PathNavigation;
/*     */     //   266: invokevirtual stop : ()V
/*     */     //   269: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #150	-> 0
/*     */     //   #151	-> 10
/*     */     //   #152	-> 18
/*     */     //   #156	-> 25
/*     */     //   #157	-> 50
/*     */     //   #158	-> 77
/*     */     //   #159	-> 104
/*     */     //   #160	-> 131
/*     */     //   #162	-> 158
/*     */     //   #163	-> 165
/*     */     //   #165	-> 172
/*     */     //   #166	-> 193
/*     */     //   #169	-> 194
/*     */     //   #170	-> 199
/*     */     //   #173	-> 208
/*     */     //   #174	-> 229
/*     */     //   #175	-> 243
/*     */     //   #178	-> 248
/*     */     //   #179	-> 261
/*     */     //   #181	-> 269
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   221	8	24	living	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   261	8	24	mob	Lnet/minecraft/world/entity/PathfinderMob;
/*     */     //   0	270	0	source	Lnet/minecraft/commands/CommandSourceStack;
/*     */     //   0	270	1	victim	Lnet/minecraft/world/entity/Entity;
/*     */     //   0	270	2	level	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   0	270	3	x	D
/*     */     //   0	270	5	y	D
/*     */     //   0	270	7	z	D
/*     */     //   0	270	9	relatives	Ljava/util/Set;
/*     */     //   0	270	10	yRot	F
/*     */     //   0	270	11	xRot	F
/*     */     //   0	270	12	lookAt	Lnet/minecraft/server/commands/LookAt;
/*     */     //   10	260	13	blockPos	Lnet/minecraft/core/BlockPos;
/*     */     //   50	220	14	relativeOrAbsoluteX	D
/*     */     //   77	193	16	relativeOrAbsoluteY	D
/*     */     //   104	166	18	relativeOrAbsoluteZ	D
/*     */     //   131	139	20	relativeOrAbsoluteYRot	F
/*     */     //   158	112	21	relativeOrAbsoluteXRot	F
/*     */     //   165	105	22	newYRot	F
/*     */     //   172	98	23	newXRot	F
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	270	9	relatives	Ljava/util/Set<Lnet/minecraft/world/entity/Relative;>; }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\TeleportCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */