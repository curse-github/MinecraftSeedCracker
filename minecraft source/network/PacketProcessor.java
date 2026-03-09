/*    */ package net.minecraft.network;
/*    */ 
/*    */ import com.google.common.collect.Queues;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.Queue;
/*    */ import java.util.concurrent.RejectedExecutionException;
/*    */ import net.minecraft.ReportedException;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketUtils;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class PacketProcessor
/*    */   implements AutoCloseable {
/* 14 */   private static final Logger LOGGER = LogUtils.getLogger(); private final Queue<ListenerAndPacket<?>> packetsToBeHandled; private final Thread runningThread; private boolean closed;
/*    */   public PacketProcessor(Thread runningThread) {
/* 16 */     this.packetsToBeHandled = Queues.newConcurrentLinkedQueue();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 21 */     this.runningThread = runningThread;
/*    */   }
/*    */ 
/*    */   
/* 25 */   public boolean isSameThread() { return (Thread.currentThread() == this.runningThread); }
/*    */ 
/*    */   
/*    */   public <T extends PacketListener> void scheduleIfPossible(T listener, Packet<T> packet) {
/* 29 */     if (this.closed) {
/* 30 */       throw new RejectedExecutionException("Server already shutting down");
/*    */     }
/* 32 */     this.packetsToBeHandled.add(new ListenerAndPacket(listener, packet));
/*    */   }
/*    */   
/*    */   public void processQueuedPackets() {
/* 36 */     if (!this.closed) {
/* 37 */       while (!this.packetsToBeHandled.isEmpty()) {
/* 38 */         ((ListenerAndPacket)this.packetsToBeHandled.poll()).handle();
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public void close() { this.closed = true; }
/*    */   private static final class ListenerAndPacket<T extends PacketListener> extends Record { private final T listener; private final Packet<T> packet;
/*    */     
/* 48 */     private ListenerAndPacket(T listener, Packet<T> packet) { this.listener = listener; this.packet = packet; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/PacketProcessor$ListenerAndPacket;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/PacketProcessor$ListenerAndPacket;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 48 */       //   0	7	0	this	Lnet/minecraft/network/PacketProcessor$ListenerAndPacket<TT;>; } public T listener() { return (T)this.listener; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/PacketProcessor$ListenerAndPacket;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/network/PacketProcessor$ListenerAndPacket;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/network/PacketProcessor$ListenerAndPacket<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/PacketProcessor$ListenerAndPacket;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #48	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/network/PacketProcessor$ListenerAndPacket;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 48 */       //   0	8	0	this	Lnet/minecraft/network/PacketProcessor$ListenerAndPacket<TT;>; } public Packet<T> packet() { return this.packet; }
/*    */ 
/*    */ 
/*    */     
/*    */     public void handle() {
/* 53 */       if (this.listener.shouldHandleMessage(this.packet)) {
/*    */         try {
/* 55 */           this.packet.handle(this.listener);
/* 56 */         } catch (Exception e) {
/* 57 */           if (e instanceof ReportedException) { ReportedException re = (ReportedException)e; if (re.getCause() instanceof OutOfMemoryError)
/* 58 */               throw PacketUtils.makeReportedException(e, this.packet, this.listener);  }
/*    */           
/* 60 */           this.listener.onPacketError(this.packet, e);
/*    */         } 
/*    */       } else {
/*    */         
/* 64 */         PacketProcessor.LOGGER.debug("Ignoring packet due to disconnection: {}", this.packet);
/*    */       } 
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\PacketProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */