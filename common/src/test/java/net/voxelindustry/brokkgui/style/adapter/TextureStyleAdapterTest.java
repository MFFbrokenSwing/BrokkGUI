package net.voxelindustry.brokkgui.style.adapter;

import net.voxelindustry.brokkgui.paint.Texture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TextureStyleAdapterTest
{
    private TextureStyleTranslator adapter;

    @Before
    public void init()
    {
        this.adapter = new TextureStyleTranslator();
    }

    @Test
    public void test()
    {
        Texture texture = new Texture("brokkgui:textures/gui/container_background.png");
        assertThat(adapter.decode("url(\"brokkgui:textures/gui/container_background.png\")")).isEqualTo(texture);

        Texture textureUV = new Texture("brokkgui:textures/gui/container_background.png", 100, 100);
        assertThat(adapter.decode("url(\"brokkgui:textures/gui/container_background.png\", 100, 100)")).isEqualTo
                (textureUV);

        Texture textureUVST = new Texture("brokkgui:textures/gui/container_background.png", 16, 16, 32, 32);
        assertThat(adapter.decode("url(\"brokkgui:textures/gui/container_background.png\", 16, 16,32, 32 )"))
                .isEqualTo(textureUVST);
    }

    @Test
    public void error()
    {
        assertThat(adapter.decode("file(brokkgui:textures/gui/container_background.png")).isNull();
    }
}
