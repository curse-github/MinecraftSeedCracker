/*     */ package net.minecraft.world.level.redstone;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.RedStoneWireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.RedstoneSide;
/*     */ 
/*     */ 
/*     */ public class ExperimentalRedstoneWireEvaluator
/*     */   extends RedstoneWireEvaluator
/*     */ {
/*  22 */   private final Deque<BlockPos> wiresToTurnOff = new ArrayDeque();
/*  23 */   private final Deque<BlockPos> wiresToTurnOn = new ArrayDeque();
/*     */   
/*  25 */   private final Object2IntMap<BlockPos> updatedWires = new Object2IntLinkedOpenHashMap();
/*     */ 
/*     */   
/*  28 */   public ExperimentalRedstoneWireEvaluator(RedStoneWireBlock wireBlock) { super(wireBlock); }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updatePowerStrength(Level level, BlockPos initialPos, BlockState ignored, Orientation orientation, boolean shapeUpdateWiresAroundInitialPosition) {
/*  33 */     Orientation initialOrientation = getInitialOrientation(level, orientation);
/*     */     
/*  35 */     calculateCurrentChanges(level, initialPos, initialOrientation);
/*     */ 
/*     */     
/*  38 */     ObjectIterator<Object2IntMap.Entry<BlockPos>> iterator = this.updatedWires.object2IntEntrySet().iterator();
/*  39 */     boolean initialWire = true;
/*  40 */     while (iterator.hasNext()) {
/*  41 */       Object2IntMap.Entry<BlockPos> next = (Object2IntMap.Entry)iterator.next();
/*  42 */       BlockPos pos = (BlockPos)next.getKey();
/*  43 */       int packed = next.getIntValue();
/*  44 */       int newLevel = unpackPower(packed);
/*  45 */       BlockState state = level.getBlockState(pos);
/*  46 */       if (state.is(this.wireBlock) && !((Integer)state.getValue(RedStoneWireBlock.POWER)).equals(Integer.valueOf(newLevel))) {
/*     */         
/*  48 */         int updateFlags = 2;
/*  49 */         if (!shapeUpdateWiresAroundInitialPosition || !initialWire) {
/*  50 */           updateFlags |= 0x80;
/*     */         }
/*  52 */         level.setBlock(pos, (BlockState)state.setValue(RedStoneWireBlock.POWER, Integer.valueOf(newLevel)), updateFlags);
/*     */       } else {
/*  54 */         iterator.remove();
/*     */       } 
/*  56 */       initialWire = false;
/*     */     } 
/*     */     
/*  59 */     causeNeighborUpdates(level);
/*     */   }
/*     */   
/*     */   private void causeNeighborUpdates(Level level) {
/*  63 */     this.updatedWires.forEach((wirePos, packed) -> {
/*  64 */           Orientation orientation = unpackOrientation(packed);
/*  65 */           BlockState state = level.getBlockState(wirePos);
/*  66 */           for (Direction neighborDirection : orientation.getDirections()) {
/*  67 */             if (isConnected(state, neighborDirection)) {
/*  68 */               BlockPos neighborPos = wirePos.relative(neighborDirection);
/*  69 */               BlockState neighborState = level.getBlockState(neighborPos);
/*  70 */               Orientation neighborOrientation = orientation.withFrontPreserveUp(neighborDirection);
/*  71 */               level.neighborChanged(neighborState, neighborPos, this.wireBlock, neighborOrientation, false);
/*  72 */               if (neighborState.isRedstoneConductor(level, neighborPos)) {
/*  73 */                 for (Direction direction : neighborOrientation.getDirections()) {
/*  74 */                   if (direction != neighborDirection.getOpposite()) {
/*  75 */                     level.neighborChanged(neighborPos.relative(direction), this.wireBlock, neighborOrientation.withFrontPreserveUp(direction));
/*     */                   }
/*     */                 } 
/*     */               }
/*     */             } 
/*     */           } 
/*     */         });
/*  82 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (serverLevel.debugSynchronizers().hasAnySubscriberFor(DebugSubscriptions.REDSTONE_WIRE_ORIENTATIONS)) {
/*  83 */         this.updatedWires.forEach((wirePos, packed) -> 
/*  84 */             serverLevel.debugSynchronizers().sendBlockValue(wirePos, DebugSubscriptions.REDSTONE_WIRE_ORIENTATIONS, unpackOrientation(packed)));
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   private static boolean isConnected(BlockState state, Direction direction) {
/*  90 */     EnumProperty<RedstoneSide> property = (EnumProperty)RedStoneWireBlock.PROPERTY_BY_DIRECTION.get(direction);
/*  91 */     if (property == null) {
/*  92 */       return (direction == Direction.DOWN);
/*     */     }
/*  94 */     return ((RedstoneSide)state.getValue(property)).isConnected();
/*     */   }
/*     */   
/*     */   private static Orientation getInitialOrientation(Level level, Orientation incomingOrigination) {
/*     */     Orientation orientation;
/*  99 */     if (incomingOrigination != null) {
/* 100 */       orientation = incomingOrigination;
/*     */     } else {
/* 102 */       orientation = Orientation.random(level.random);
/*     */     } 
/* 104 */     return orientation.withUp(Direction.UP).withSideBias(Orientation.SideBias.LEFT);
/*     */   }
/*     */ 
/*     */   
/*     */   private void calculateCurrentChanges(Level level, BlockPos initialPosition, Orientation initialOrientation) {
/* 109 */     BlockState initialState = level.getBlockState(initialPosition);
/* 110 */     if (initialState.is(this.wireBlock)) {
/* 111 */       setPower(initialPosition, ((Integer)initialState.getValue(RedStoneWireBlock.POWER)).intValue(), initialOrientation);
/* 112 */       this.wiresToTurnOff.add(initialPosition);
/*     */     } else {
/*     */       
/* 115 */       propagateChangeToNeighbors(level, initialPosition, 0, initialOrientation, true);
/*     */     } 
/*     */     
/* 118 */     while (!this.wiresToTurnOff.isEmpty()) {
/* 119 */       int powerToSet; BlockPos pos = (BlockPos)this.wiresToTurnOff.removeFirst();
/* 120 */       int packed = this.updatedWires.getInt(pos);
/* 121 */       Orientation orientation = unpackOrientation(packed);
/* 122 */       int oldPower = unpackPower(packed);
/* 123 */       int blockPower = getBlockSignal(level, pos);
/* 124 */       int wirePower = getIncomingWireSignal(level, pos);
/* 125 */       int newPower = Math.max(blockPower, wirePower);
/*     */ 
/*     */       
/* 128 */       if (newPower < oldPower) {
/* 129 */         if (blockPower > 0 && !this.wiresToTurnOn.contains(pos)) {
/* 130 */           this.wiresToTurnOn.add(pos);
/*     */         }
/*     */         
/* 133 */         powerToSet = 0;
/*     */       } else {
/* 135 */         powerToSet = newPower;
/*     */       } 
/* 137 */       if (powerToSet != oldPower) {
/* 138 */         setPower(pos, powerToSet, orientation);
/*     */       }
/* 140 */       propagateChangeToNeighbors(level, pos, powerToSet, orientation, (oldPower > newPower));
/*     */     } 
/* 142 */     while (!this.wiresToTurnOn.isEmpty()) {
/* 143 */       BlockPos pos = (BlockPos)this.wiresToTurnOn.removeFirst();
/* 144 */       int packed = this.updatedWires.getInt(pos);
/* 145 */       int oldPower = unpackPower(packed);
/* 146 */       int blockPower = getBlockSignal(level, pos);
/* 147 */       int wirePower = getIncomingWireSignal(level, pos);
/* 148 */       int newPower = Math.max(blockPower, wirePower);
/*     */       
/* 150 */       Orientation orientation = unpackOrientation(packed);
/* 151 */       if (newPower > oldPower) {
/* 152 */         setPower(pos, newPower, orientation);
/* 153 */       } else if (newPower < oldPower) {
/* 154 */         throw new IllegalStateException("Turning off wire while trying to turn it on. Should not happen.");
/*     */       } 
/* 156 */       propagateChangeToNeighbors(level, pos, newPower, orientation, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/* 161 */   private static int packOrientationAndPower(Orientation orientation, int power) { return orientation.getIndex() << 4 | power; }
/*     */ 
/*     */ 
/*     */   
/* 165 */   private static Orientation unpackOrientation(int packed) { return Orientation.fromIndex(packed >> 4); }
/*     */ 
/*     */ 
/*     */   
/* 169 */   private static int unpackPower(int packed) { return packed & 0xF; }
/*     */ 
/*     */   
/*     */   private void setPower(BlockPos pos, int newPower, Orientation orientation) {
/* 173 */     this.updatedWires.compute(pos, (key, packed) -> {
/* 174 */           if (packed == null) {
/* 175 */             return Integer.valueOf(packOrientationAndPower(orientation, newPower));
/*     */           }
/* 177 */           return Integer.valueOf(packOrientationAndPower(unpackOrientation(packed.intValue()), newPower));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void propagateChangeToNeighbors(Level level, BlockPos pos, int newPower, Orientation orientation, boolean allowTurningOff) {
/* 183 */     for (Direction directionHorizontal : orientation.getHorizontalDirections()) {
/* 184 */       BlockPos offsetPos = pos.relative(directionHorizontal);
/* 185 */       enqueueNeighborWire(level, offsetPos, newPower, orientation.withFront(directionHorizontal), allowTurningOff);
/*     */     } 
/* 187 */     for (Direction directionVertical : orientation.getVerticalDirections()) {
/* 188 */       BlockPos offsetPos = pos.relative(directionVertical);
/* 189 */       boolean solidBlock = level.getBlockState(offsetPos).isRedstoneConductor(level, offsetPos);
/* 190 */       for (Direction directionHorizontal : orientation.getHorizontalDirections()) {
/*     */ 
/*     */         
/* 193 */         BlockPos neighbor = pos.relative(directionHorizontal);
/* 194 */         if (directionVertical == Direction.UP && !solidBlock) {
/* 195 */           BlockPos neighborWire = offsetPos.relative(directionHorizontal);
/* 196 */           enqueueNeighborWire(level, neighborWire, newPower, orientation.withFront(directionHorizontal), allowTurningOff); continue;
/* 197 */         }  if (directionVertical == Direction.DOWN && !level.getBlockState(neighbor).isRedstoneConductor(level, neighbor)) {
/*     */           
/* 199 */           BlockPos neighborWire = offsetPos.relative(directionHorizontal);
/* 200 */           enqueueNeighborWire(level, neighborWire, newPower, orientation.withFront(directionHorizontal), allowTurningOff);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void enqueueNeighborWire(Level level, BlockPos pos, int newFromPower, Orientation orientation, boolean allowTurningOff) {
/* 207 */     BlockState state = level.getBlockState(pos);
/* 208 */     if (state.is(this.wireBlock)) {
/* 209 */       int toPower = getWireSignal(pos, state);
/*     */       
/* 211 */       if (toPower < newFromPower - 1 && !this.wiresToTurnOn.contains(pos)) {
/*     */         
/* 213 */         this.wiresToTurnOn.add(pos);
/* 214 */         setPower(pos, toPower, orientation);
/*     */       } 
/* 216 */       if (allowTurningOff)
/*     */       {
/* 218 */         if (toPower > newFromPower && !this.wiresToTurnOff.contains(pos)) {
/*     */           
/* 220 */           this.wiresToTurnOff.add(pos);
/* 221 */           setPower(pos, toPower, orientation);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getWireSignal(BlockPos pos, BlockState state) {
/* 229 */     int packed = this.updatedWires.getOrDefault(pos, -1);
/* 230 */     if (packed != -1) {
/* 231 */       return unpackPower(packed);
/*     */     }
/* 233 */     return super.getWireSignal(pos, state);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\redstone\ExperimentalRedstoneWireEvaluator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */