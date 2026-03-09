/*     */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Relative;
/*     */ import net.minecraft.world.entity.monster.Endermite;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.portal.TeleportTransition;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThrownEnderpearl
/*     */   extends ThrowableItemProjectile
/*     */ {
/*  34 */   private long ticketTimer = 0L;
/*     */ 
/*     */   
/*  37 */   public ThrownEnderpearl(EntityType<? extends ThrownEnderpearl> type, Level level) { super(type, level); }
/*     */ 
/*     */ 
/*     */   
/*  41 */   public ThrownEnderpearl(Level level, LivingEntity mob, ItemStack itemStack) { super(EntityType.ENDER_PEARL, mob, level, itemStack); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   protected Item getDefaultItem() { return Items.ENDER_PEARL; }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setOwner(EntityReference<Entity> owner) {
/*  51 */     deregisterFromCurrentOwner();
/*  52 */     super.setOwner(owner);
/*  53 */     registerToCurrentOwner();
/*     */   }
/*     */   
/*     */   private void deregisterFromCurrentOwner() {
/*  57 */     Entity entity = getOwner(); if (entity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity;
/*  58 */       serverPlayer.deregisterEnderPearl(this); }
/*     */   
/*     */   }
/*     */   
/*     */   private void registerToCurrentOwner() {
/*  63 */     Entity entity = getOwner(); if (entity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity;
/*  64 */       serverPlayer.registerEnderPearl(this); }
/*     */   
/*     */   }
/*     */   
/*     */   public Entity getOwner() {
/*     */     ServerLevel serverLevel;
/*  70 */     if (this.owner != null) { Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*  71 */       else { return super.getOwner(); }  } else { return super.getOwner(); }
/*     */     
/*  73 */     return (Entity)this.owner.getEntity(serverLevel, Entity.class);
/*     */   }
/*     */   
/*     */   private static Entity findOwnerIncludingDeadPlayer(ServerLevel serverLevel, UUID uuid) {
/*  77 */     Entity owner = serverLevel.getEntityInAnyDimension(uuid);
/*  78 */     if (owner != null) {
/*  79 */       return owner;
/*     */     }
/*     */ 
/*     */     
/*  83 */     return serverLevel.getServer().getPlayerList().getPlayer(uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitEntity(EntityHitResult hitResult) {
/*  88 */     super.onHitEntity(hitResult);
/*  89 */     hitResult.getEntity().hurt(damageSources().thrown(this, getOwner()), 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHit(HitResult hitResult) {
/*  94 */     super.onHit(hitResult);
/*     */     
/*  96 */     for (int i = 0; i < 32; i++) {
/*  97 */       level().addParticle(ParticleTypes.PORTAL, getX(), getY() + this.random.nextDouble() * 2.0D, getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
/*     */     }
/*     */     
/* 100 */     Level level1 = level(); if (level1 instanceof ServerLevel) { ServerLevel level = (ServerLevel)level1; if (!isRemoved()) {
/*     */ 
/*     */ 
/*     */         
/* 104 */         Entity owner = getOwner();
/* 105 */         if (owner == null || !isAllowedToTeleportOwner(owner, level)) {
/* 106 */           discard();
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 112 */         Vec3 teleportPos = oldPosition();
/*     */         
/* 114 */         if (owner instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)owner;
/* 115 */           if (player.connection.isAcceptingMessages()) {
/* 116 */             if (this.random.nextFloat() < 0.05F && level.isSpawningMonsters()) {
/* 117 */               Endermite endermite = (Endermite)EntityType.ENDERMITE.create(level, EntitySpawnReason.TRIGGERED);
/* 118 */               if (endermite != null) {
/* 119 */                 endermite.snapTo(owner.getX(), owner.getY(), owner.getZ(), owner.getYRot(), owner.getXRot());
/* 120 */                 level.addFreshEntity(endermite);
/*     */               } 
/*     */             } 
/*     */             
/* 124 */             if (isOnPortalCooldown())
/*     */             {
/*     */               
/* 127 */               owner.setPortalCooldown();
/*     */             }
/*     */             
/* 130 */             ServerPlayer newOwner = player.teleport(new TeleportTransition(level, teleportPos, Vec3.ZERO, 0.0F, 0.0F, Relative.union(new Set[] { Relative.ROTATION, Relative.DELTA }, ), TeleportTransition.DO_NOTHING));
/* 131 */             if (newOwner != null) {
/* 132 */               newOwner.resetFallDistance();
/* 133 */               newOwner.resetCurrentImpulseContext();
/* 134 */               newOwner.hurtServer(player.level(), damageSources().enderPearl(), 5.0F);
/*     */             } 
/*     */             
/* 137 */             playSound(level, teleportPos);
/*     */           }  }
/*     */         else
/* 140 */         { Entity newOwner = owner.teleport(new TeleportTransition(level, teleportPos, owner.getDeltaMovement(), owner.getYRot(), owner.getXRot(), TeleportTransition.DO_NOTHING));
/* 141 */           if (newOwner != null) {
/* 142 */             newOwner.resetFallDistance();
/*     */           }
/* 144 */           playSound(level, teleportPos); }
/*     */ 
/*     */         
/* 147 */         discard();
/*     */         return;
/*     */       }  }
/*     */      } private static boolean isAllowedToTeleportOwner(Entity owner, Level newLevel) {
/* 151 */     if (owner.level().dimension() == newLevel.dimension()) {
/* 152 */       if (owner instanceof LivingEntity) { LivingEntity livingOwner = (LivingEntity)owner;
/* 153 */         return (livingOwner.isAlive() && !livingOwner.isSleeping()); }
/*     */       
/* 155 */       return owner.isAlive();
/*     */     } 
/* 157 */     return owner.canUsePortal(true);
/*     */   }
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
/*     */ 
/*     */   
/*     */   public void tick() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   4: astore_2
/*     */     //   5: aload_2
/*     */     //   6: instanceof net/minecraft/server/level/ServerLevel
/*     */     //   9: ifeq -> 20
/*     */     //   12: aload_2
/*     */     //   13: checkcast net/minecraft/server/level/ServerLevel
/*     */     //   16: astore_1
/*     */     //   17: goto -> 25
/*     */     //   20: aload_0
/*     */     //   21: invokespecial tick : ()V
/*     */     //   24: return
/*     */     //   25: aload_0
/*     */     //   26: invokevirtual position : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   29: invokevirtual x : ()D
/*     */     //   32: invokestatic blockToSectionCoord : (D)I
/*     */     //   35: istore_2
/*     */     //   36: aload_0
/*     */     //   37: invokevirtual position : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   40: invokevirtual z : ()D
/*     */     //   43: invokestatic blockToSectionCoord : (D)I
/*     */     //   46: istore_3
/*     */     //   47: aload_0
/*     */     //   48: getfield owner : Lnet/minecraft/world/entity/EntityReference;
/*     */     //   51: ifnull -> 68
/*     */     //   54: aload_1
/*     */     //   55: aload_0
/*     */     //   56: getfield owner : Lnet/minecraft/world/entity/EntityReference;
/*     */     //   59: invokevirtual getUUID : ()Ljava/util/UUID;
/*     */     //   62: invokestatic findOwnerIncludingDeadPlayer : (Lnet/minecraft/server/level/ServerLevel;Ljava/util/UUID;)Lnet/minecraft/world/entity/Entity;
/*     */     //   65: goto -> 69
/*     */     //   68: aconst_null
/*     */     //   69: astore #4
/*     */     //   71: aload #4
/*     */     //   73: instanceof net/minecraft/server/level/ServerPlayer
/*     */     //   76: ifeq -> 132
/*     */     //   79: aload #4
/*     */     //   81: checkcast net/minecraft/server/level/ServerPlayer
/*     */     //   84: astore #5
/*     */     //   86: aload #4
/*     */     //   88: invokevirtual isAlive : ()Z
/*     */     //   91: ifne -> 132
/*     */     //   94: aload #5
/*     */     //   96: getfield wonGame : Z
/*     */     //   99: ifne -> 132
/*     */     //   102: aload #5
/*     */     //   104: invokevirtual level : ()Lnet/minecraft/server/level/ServerLevel;
/*     */     //   107: invokevirtual getGameRules : ()Lnet/minecraft/world/level/gamerules/GameRules;
/*     */     //   110: getstatic net/minecraft/world/level/gamerules/GameRules.ENDER_PEARLS_VANISH_ON_DEATH : Lnet/minecraft/world/level/gamerules/GameRule;
/*     */     //   113: invokevirtual get : (Lnet/minecraft/world/level/gamerules/GameRule;)Ljava/lang/Object;
/*     */     //   116: checkcast java/lang/Boolean
/*     */     //   119: invokevirtual booleanValue : ()Z
/*     */     //   122: ifeq -> 132
/*     */     //   125: aload_0
/*     */     //   126: invokevirtual discard : ()V
/*     */     //   129: goto -> 136
/*     */     //   132: aload_0
/*     */     //   133: invokespecial tick : ()V
/*     */     //   136: aload_0
/*     */     //   137: invokevirtual isAlive : ()Z
/*     */     //   140: ifne -> 144
/*     */     //   143: return
/*     */     //   144: aload_0
/*     */     //   145: invokevirtual position : ()Lnet/minecraft/world/phys/Vec3;
/*     */     //   148: invokestatic containing : (Lnet/minecraft/core/Position;)Lnet/minecraft/core/BlockPos;
/*     */     //   151: astore #5
/*     */     //   153: aload_0
/*     */     //   154: dup
/*     */     //   155: getfield ticketTimer : J
/*     */     //   158: lconst_1
/*     */     //   159: lsub
/*     */     //   160: dup2_x1
/*     */     //   161: putfield ticketTimer : J
/*     */     //   164: lconst_0
/*     */     //   165: lcmp
/*     */     //   166: ifle -> 193
/*     */     //   169: iload_2
/*     */     //   170: aload #5
/*     */     //   172: invokevirtual getX : ()I
/*     */     //   175: invokestatic blockToSectionCoord : (I)I
/*     */     //   178: if_icmpne -> 193
/*     */     //   181: iload_3
/*     */     //   182: aload #5
/*     */     //   184: invokevirtual getZ : ()I
/*     */     //   187: invokestatic blockToSectionCoord : (I)I
/*     */     //   190: if_icmpeq -> 218
/*     */     //   193: aload #4
/*     */     //   195: instanceof net/minecraft/server/level/ServerPlayer
/*     */     //   198: ifeq -> 218
/*     */     //   201: aload #4
/*     */     //   203: checkcast net/minecraft/server/level/ServerPlayer
/*     */     //   206: astore #6
/*     */     //   208: aload_0
/*     */     //   209: aload #6
/*     */     //   211: aload_0
/*     */     //   212: invokevirtual registerAndUpdateEnderPearlTicket : (Lnet/minecraft/world/entity/projectile/throwableitemprojectile/ThrownEnderpearl;)J
/*     */     //   215: putfield ticketTimer : J
/*     */     //   218: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #162	-> 0
/*     */     //   #163	-> 20
/*     */     //   #164	-> 24
/*     */     //   #167	-> 25
/*     */     //   #168	-> 36
/*     */     //   #171	-> 47
/*     */     //   #173	-> 71
/*     */     //   #174	-> 125
/*     */     //   #176	-> 132
/*     */     //   #179	-> 136
/*     */     //   #180	-> 143
/*     */     //   #183	-> 144
/*     */     //   #184	-> 153
/*     */     //   #185	-> 184
/*     */     //   #186	-> 193
/*     */     //   #187	-> 208
/*     */     //   #190	-> 218
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   17	3	1	serverLevel	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   86	46	5	serverPlayer	Lnet/minecraft/server/level/ServerPlayer;
/*     */     //   208	10	6	serverPlayer	Lnet/minecraft/server/level/ServerPlayer;
/*     */     //   0	219	0	this	Lnet/minecraft/world/entity/projectile/throwableitemprojectile/ThrownEnderpearl;
/*     */     //   25	194	1	serverLevel	Lnet/minecraft/server/level/ServerLevel;
/*     */     //   36	183	2	previousChunkX	I
/*     */     //   47	172	3	previousChunkZ	I
/*     */     //   71	148	4	owner	Lnet/minecraft/world/entity/Entity;
/*     */     //   153	66	5	currentPos	Lnet/minecraft/core/BlockPos; }
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
/*     */   
/* 193 */   private void playSound(Level level, Vec3 position) { level.playSound(null, position.x, position.y, position.z, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS); }
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity teleport(TeleportTransition transition) {
/* 198 */     Entity newEntity = super.teleport(transition);
/* 199 */     if (newEntity != null)
/*     */     {
/*     */ 
/*     */       
/* 203 */       newEntity.placePortalTicket(BlockPos.containing(newEntity.position()));
/*     */     }
/* 205 */     return newEntity;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTeleport(Level from, Level to) {
/* 210 */     if (from.dimension() == Level.END && to.dimension() == Level.OVERWORLD) { Entity entity = getOwner(); if (entity instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)entity;
/* 211 */         return (super.canTeleport(from, to) && player.seenCredits); }
/*     */        }
/* 213 */      return super.canTeleport(from, to);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onInsideBlock(BlockState state) {
/* 218 */     super.onInsideBlock(state);
/* 219 */     if (state.is(Blocks.END_GATEWAY)) { Entity entity = getOwner(); if (entity instanceof ServerPlayer) { ServerPlayer player = (ServerPlayer)entity;
/* 220 */         player.onInsideBlock(state); }
/*     */        }
/*     */   
/*     */   }
/*     */   
/*     */   public void onRemoval(Entity.RemovalReason reason) {
/* 226 */     if (reason != Entity.RemovalReason.UNLOADED_WITH_PLAYER) {
/* 227 */       deregisterFromCurrentOwner();
/*     */     }
/* 229 */     super.onRemoval(reason);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 234 */   public void onAboveBubbleColumn(boolean dragDown, BlockPos pos) { Entity.handleOnAboveBubbleColumn(this, dragDown, pos); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 239 */   public void onInsideBubbleColumn(boolean dragDown) { Entity.handleOnInsideBubbleColumn(this, dragDown); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\projectile\throwableitemprojectile\ThrownEnderpearl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */