/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ 
/*    */ public class NopProcessor extends StructureProcessor {
/*  6 */   public static final MapCodec<NopProcessor> CODEC = MapCodec.unit(() -> INSTANCE);
/*    */   
/*  8 */   public static final NopProcessor INSTANCE = new NopProcessor();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   protected StructureProcessorType<?> getType() { return StructureProcessorType.NOP; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\templatesystem\NopProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */