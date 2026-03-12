/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.entity.Entity;
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
/*     */ public final class Path
/*     */ {
/*  24 */   public static final StreamCodec<FriendlyByteBuf, Path> STREAM_CODEC = StreamCodec.of((output, value) -> value.writeToStream(output), Path::createFromStream);
/*     */   
/*     */   private final List<Node> nodes;
/*     */   
/*     */   private DebugData debugData;
/*     */   
/*     */   private int nextNodeIndex;
/*     */   private final BlockPos target;
/*     */   private final float distToTarget;
/*     */   private final boolean reached;
/*     */   
/*     */   public Path(List<Node> nodes, BlockPos target, boolean reached) {
/*  36 */     this.nodes = nodes;
/*  37 */     this.target = target;
/*     */     
/*  39 */     this.distToTarget = nodes.isEmpty() ? Float.MAX_VALUE : ((Node)this.nodes.get(this.nodes.size() - 1)).distanceManhattan(this.target);
/*     */     
/*  41 */     this.reached = reached;
/*     */   }
/*     */ 
/*     */   
/*  45 */   public void advance() { this.nextNodeIndex++; }
/*     */ 
/*     */ 
/*     */   
/*  49 */   public boolean notStarted() { return (this.nextNodeIndex <= 0); }
/*     */ 
/*     */ 
/*     */   
/*  53 */   public boolean isDone() { return (this.nextNodeIndex >= this.nodes.size()); }
/*     */ 
/*     */   
/*     */   public Node getEndNode() {
/*  57 */     if (!this.nodes.isEmpty()) {
/*  58 */       return (Node)this.nodes.get(this.nodes.size() - 1);
/*     */     }
/*  60 */     return null;
/*     */   }
/*     */ 
/*     */   
/*  64 */   public Node getNode(int i) { return (Node)this.nodes.get(i); }
/*     */ 
/*     */   
/*     */   public void truncateNodes(int index) {
/*  68 */     if (this.nodes.size() > index) {
/*  69 */       this.nodes.subList(index, this.nodes.size()).clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  74 */   public void replaceNode(int index, Node replaceWith) { this.nodes.set(index, replaceWith); }
/*     */ 
/*     */ 
/*     */   
/*  78 */   public int getNodeCount() { return this.nodes.size(); }
/*     */ 
/*     */ 
/*     */   
/*  82 */   public int getNextNodeIndex() { return this.nextNodeIndex; }
/*     */ 
/*     */ 
/*     */   
/*  86 */   public void setNextNodeIndex(int nextNodeIndex) { this.nextNodeIndex = nextNodeIndex; }
/*     */ 
/*     */   
/*     */   public Vec3 getEntityPosAtNode(Entity entity, int index) {
/*  90 */     Node node = (Node)this.nodes.get(index);
/*  91 */     double x = node.x + (int)(entity.getBbWidth() + 1.0F) * 0.5D;
/*  92 */     double y = node.y;
/*  93 */     double z = node.z + (int)(entity.getBbWidth() + 1.0F) * 0.5D;
/*  94 */     return new Vec3(x, y, z);
/*     */   }
/*     */ 
/*     */   
/*  98 */   public BlockPos getNodePos(int index) { return ((Node)this.nodes.get(index)).asBlockPos(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 105 */   public Vec3 getNextEntityPos(Entity entity) { return getEntityPosAtNode(entity, this.nextNodeIndex); }
/*     */ 
/*     */ 
/*     */   
/* 109 */   public BlockPos getNextNodePos() { return ((Node)this.nodes.get(this.nextNodeIndex)).asBlockPos(); }
/*     */ 
/*     */ 
/*     */   
/* 113 */   public Node getNextNode() { return (Node)this.nodes.get(this.nextNodeIndex); }
/*     */ 
/*     */ 
/*     */   
/* 117 */   public Node getPreviousNode() { return (this.nextNodeIndex > 0) ? (Node)this.nodes.get(this.nextNodeIndex - 1) : null; }
/*     */ 
/*     */ 
/*     */   
/* 121 */   public boolean sameAs(Path path) { return (path != null && this.nodes.equals(path.nodes)); }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*     */     Path path;
/* 126 */     if (obj instanceof Path) { path = (Path)obj; }
/* 127 */     else { return false; }
/*     */     
/* 129 */     return (this.nextNodeIndex == path.nextNodeIndex && this.debugData == path.debugData && this.reached == path.reached && this.target
/*     */ 
/*     */       
/* 132 */       .equals(path.target) && this.nodes
/* 133 */       .equals(path.nodes));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 138 */   public int hashCode() { return this.nextNodeIndex + this.nodes.hashCode() * 31; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   public boolean canReach() { return this.reached; }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/* 150 */   void setDebug(Node[] openSet, Node[] closedSet, Set<Target> targets) { this.debugData = new DebugData(openSet, closedSet, targets); }
/*     */ 
/*     */ 
/*     */   
/* 154 */   public DebugData debugData() { return this.debugData; }
/*     */ 
/*     */   
/*     */   public void writeToStream(FriendlyByteBuf buffer) {
/* 158 */     if (this.debugData == null || this.debugData.targetNodes.isEmpty()) {
/* 159 */       throw new IllegalStateException("Missing debug data");
/*     */     }
/*     */     
/* 162 */     buffer.writeBoolean(this.reached);
/* 163 */     buffer.writeInt(this.nextNodeIndex);
/* 164 */     buffer.writeBlockPos(this.target);
/* 165 */     buffer.writeCollection(this.nodes, (out, node) -> node.writeToStream(out));
/* 166 */     this.debugData.write(buffer);
/*     */   }
/*     */   
/*     */   public static Path createFromStream(FriendlyByteBuf buffer) {
/* 170 */     boolean reached = buffer.readBoolean();
/* 171 */     int indexStream = buffer.readInt();
/* 172 */     BlockPos target = buffer.readBlockPos();
/* 173 */     List<Node> nodes = buffer.readList(Node::createFromStream);
/* 174 */     DebugData debugData = DebugData.read(buffer);
/*     */     
/* 176 */     Path path = new Path(nodes, target, reached);
/* 177 */     path.debugData = debugData;
/* 178 */     path.nextNodeIndex = indexStream;
/*     */     
/* 180 */     return path;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 185 */   public String toString() { return "Path(length=" + this.nodes.size() + ")"; }
/*     */ 
/*     */ 
/*     */   
/* 189 */   public BlockPos getTarget() { return this.target; }
/*     */ 
/*     */ 
/*     */   
/* 193 */   public float getDistToTarget() { return this.distToTarget; }
/*     */ 
/*     */   
/*     */   private static Node[] readNodeArray(FriendlyByteBuf input) {
/* 197 */     Node[] nodes = new Node[input.readVarInt()];
/* 198 */     for (int i = 0; i < nodes.length; i++) {
/* 199 */       nodes[i] = Node.createFromStream(input);
/*     */     }
/* 201 */     return nodes;
/*     */   }
/*     */   
/*     */   private static void writeNodeArray(FriendlyByteBuf output, Node[] nodes) {
/* 205 */     output.writeVarInt(nodes.length);
/* 206 */     for (Node node : nodes) {
/* 207 */       node.writeToStream(output);
/*     */     }
/*     */   }
/*     */   
/*     */   public Path copy() {
/* 212 */     Path result = new Path(this.nodes, this.target, this.reached);
/* 213 */     result.debugData = this.debugData;
/* 214 */     result.nextNodeIndex = this.nextNodeIndex;
/* 215 */     return result;
/*     */   }
/*     */   public static final class DebugData extends Record { private final Node[] openSet; private final Node[] closedSet; private final Set<Target> targetNodes;
/* 218 */     public DebugData(Node[] openSet, Node[] closedSet, Set<Target> targetNodes) { this.openSet = openSet; this.closedSet = closedSet; this.targetNodes = targetNodes; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/pathfinder/Path$DebugData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #218	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 218 */       //   0	7	0	this	Lnet/minecraft/world/level/pathfinder/Path$DebugData; } public Node[] openSet() { return this.openSet; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/pathfinder/Path$DebugData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #218	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/pathfinder/Path$DebugData; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/pathfinder/Path$DebugData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #218	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/pathfinder/Path$DebugData;
/* 218 */       //   0	8	1	o	Ljava/lang/Object; } public Node[] closedSet() { return this.closedSet; } public Set<Target> targetNodes() { return this.targetNodes; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(FriendlyByteBuf output) {
/* 225 */       output.writeCollection(this.targetNodes, (out, target) -> target.writeToStream(out));
/* 226 */       Path.writeNodeArray(output, this.openSet);
/* 227 */       Path.writeNodeArray(output, this.closedSet);
/*     */     }
/*     */     
/*     */     public static DebugData read(FriendlyByteBuf input) {
/* 231 */       HashSet<Target> targets = (HashSet)input.readCollection(HashSet::new, Target::createFromStream);
/* 232 */       Node[] openSet = Path.readNodeArray(input);
/* 233 */       Node[] closedSet = Path.readNodeArray(input);
/* 234 */       return new DebugData(openSet, closedSet, targets);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\pathfinder\Path.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */