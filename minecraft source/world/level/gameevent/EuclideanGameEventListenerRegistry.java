/*     */ package net.minecraft.world.level.gameevent;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.debug.DebugGameEventListenerInfo;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EuclideanGameEventListenerRegistry implements GameEventListenerRegistry {
/*     */   private final List<GameEventListener> listeners;
/*     */   
/*     */   public EuclideanGameEventListenerRegistry(ServerLevel level, int sectionY, OnEmptyAction onEmptyAction) {
/*  19 */     this.listeners = Lists.newArrayList();
/*  20 */     this.listenersToRemove = Sets.newHashSet();
/*  21 */     this.listenersToAdd = Lists.newArrayList();
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
/*  34 */     this.level = level;
/*  35 */     this.sectionY = sectionY;
/*  36 */     this.onEmptyAction = onEmptyAction;
/*     */   }
/*     */   private final Set<GameEventListener> listenersToRemove; private final List<GameEventListener> listenersToAdd; private boolean processing; private final ServerLevel level; private final int sectionY;
/*     */   private final OnEmptyAction onEmptyAction;
/*     */   
/*  41 */   public boolean isEmpty() { return this.listeners.isEmpty(); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(GameEventListener listener) {
/*  46 */     if (this.processing) {
/*  47 */       this.listenersToAdd.add(listener);
/*     */     } else {
/*  49 */       this.listeners.add(listener);
/*     */     } 
/*  51 */     sendDebugInfo(this.level, listener);
/*     */   }
/*     */   
/*     */   private static void sendDebugInfo(ServerLevel level, GameEventListener listener) {
/*  55 */     if (!level.debugSynchronizers().hasAnySubscriberFor(DebugSubscriptions.GAME_EVENT_LISTENERS)) {
/*     */       return;
/*     */     }
/*     */     
/*  59 */     DebugGameEventListenerInfo info = new DebugGameEventListenerInfo(listener.getListenerRadius());
/*  60 */     PositionSource listenerSource = listener.getListenerSource();
/*  61 */     if (listenerSource instanceof BlockPositionSource) { BlockPositionSource blockSource = (BlockPositionSource)listenerSource;
/*  62 */       level.debugSynchronizers().sendBlockValue(blockSource.pos(), DebugSubscriptions.GAME_EVENT_LISTENERS, info); }
/*  63 */     else if (listenerSource instanceof EntityPositionSource) { EntityPositionSource entitySource = (EntityPositionSource)listenerSource;
/*  64 */       Entity entity = level.getEntity(entitySource.getUuid());
/*  65 */       if (entity != null) {
/*  66 */         level.debugSynchronizers().sendEntityValue(entity, DebugSubscriptions.GAME_EVENT_LISTENERS, info);
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void unregister(GameEventListener listener) {
/*  73 */     if (this.processing) {
/*  74 */       this.listenersToRemove.add(listener);
/*     */     } else {
/*  76 */       this.listeners.remove(listener);
/*     */     } 
/*     */     
/*  79 */     if (this.listeners.isEmpty()) {
/*  80 */       this.onEmptyAction.apply(this.sectionY);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean visitInRangeListeners(Holder<GameEvent> event, Vec3 sourcePosition, GameEvent.Context context, GameEventListenerRegistry.ListenerVisitor action) {
/*  86 */     this.processing = true;
/*  87 */     boolean applicable = false;
/*     */     try {
/*  89 */       for (Iterator<GameEventListener> iterator = this.listeners.iterator(); iterator.hasNext(); ) {
/*  90 */         GameEventListener listener = (GameEventListener)iterator.next();
/*  91 */         if (this.listenersToRemove.remove(listener)) {
/*  92 */           iterator.remove();
/*     */           
/*     */           continue;
/*     */         } 
/*  96 */         Optional<Vec3> optionalPosition = getPostableListenerPosition(this.level, sourcePosition, listener);
/*  97 */         if (optionalPosition.isPresent()) {
/*  98 */           action.visit(listener, (Vec3)optionalPosition.get());
/*  99 */           applicable = true;
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 103 */       this.processing = false;
/*     */     } 
/*     */     
/* 106 */     if (!this.listenersToAdd.isEmpty()) {
/* 107 */       this.listeners.addAll(this.listenersToAdd);
/* 108 */       this.listenersToAdd.clear();
/*     */     } 
/*     */     
/* 111 */     if (!this.listenersToRemove.isEmpty()) {
/* 112 */       this.listeners.removeAll(this.listenersToRemove);
/* 113 */       this.listenersToRemove.clear();
/*     */     } 
/* 115 */     return applicable;
/*     */   }
/*     */   
/*     */   private static Optional<Vec3> getPostableListenerPosition(ServerLevel level, Vec3 sourcePosition, GameEventListener listener) {
/* 119 */     Optional<Vec3> position = listener.getListenerSource().getPosition(level);
/*     */     
/* 121 */     if (position.isEmpty()) {
/* 122 */       return Optional.empty();
/*     */     }
/*     */     
/* 125 */     double distanceFromOrigin = BlockPos.containing((Position)position.get()).distSqr(BlockPos.containing(sourcePosition));
/* 126 */     int radiusSqr = listener.getListenerRadius() * listener.getListenerRadius();
/*     */     
/* 128 */     if (distanceFromOrigin > radiusSqr) {
/* 129 */       return Optional.empty();
/*     */     }
/* 131 */     return position;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface OnEmptyAction {
/*     */     void apply(int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\gameevent\EuclideanGameEventListenerRegistry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */