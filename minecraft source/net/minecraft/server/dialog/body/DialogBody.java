/*    */ package net.minecraft.server.dialog.body;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ 
/*    */ public interface DialogBody
/*    */ {
/* 12 */   public static final Codec<DialogBody> DIALOG_BODY_CODEC = BuiltInRegistries.DIALOG_BODY_TYPE.byNameCodec().dispatch(DialogBody::mapCodec, c -> c);
/*    */   
/* 14 */   public static final Codec<List<DialogBody>> COMPACT_LIST_CODEC = ExtraCodecs.compactListCodec(DIALOG_BODY_CODEC);
/*    */   
/*    */   MapCodec<? extends DialogBody> mapCodec();
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\dialog\body\DialogBody.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */