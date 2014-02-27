package net.luminis.xmlbuilder;

import static net.luminis.xmlbuilder.Xml.*;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class XmlTest {

    @Test
    public void shouldGenerateEmptyXml() {
        assertThat(xml("empty")
                .toString(),
                is("<empty/>"));
    }

    @Test
    public void shouldGenerateXmlWithAttributes() {
        assertThat(xml("withAttributes")
                .attribute("attr1", "val1")
                .attribute("attr2", "val2")
                .toString(),
                is("<withAttributes attr1=\"val1\" attr2=\"val2\"/>"));
    }

    @Test
    public void shouldGenerateXmlWithChildren() {
        assertThat(xml("withChildren")
                .add(xml("child"))
                .toString(),
                is("<withChildren><child/></withChildren>"));
    }

    @Test
    public void shouldGenerateXmlWithValue() {
        assertThat(xml("withValue", "morality")
                .toString(),
                is("<withValue>morality</withValue>"));
    }

    @Test
    public void shouldGenerateXmlWithCDAtaValue() {
        assertThat(xml("withValue", "morality", true)
                .toString(),
                is("<withValue><![CDATA[morality]]></withValue>"));
    }

}
