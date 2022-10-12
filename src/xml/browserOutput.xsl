<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ord="http://model.lb2.ua.mylaba/order/"
                xmlns:driv="http://model.lb2.ua.mylaba/driver/"
                xmlns:user="http://model.lb2.ua.mylaba/user/"
                exclude-result-prefixes="ord">

    <xsl:variable name="title">Історія викликів</xsl:variable>

    <xsl:output method="html" doctype-system="about:legacy-compat"
                indent="yes" include-content-type="yes"/>

    <!-- Another way output HTML 5 Doctype -->
    <!-- Not all parsers support "disable-output-escaping" -->
    <!-- <xsl:text disable-output-escaping="yes">&lt;!DOCTYPE html&gt;</xsl:text> -->
    <xsl:template match="/">
        <html>
            <style>
                body{
                background-color:grey;
                }

                table, th, td {
                table-layout:fixed;
                border:1px solid black;
                text-align:center;
                background-color:white;
                }

                table{
                margin-bottom:30px;
                }

                .def, th{
                padding:15px;
                }

                .num{
                font-size:30px;
                }


            </style>
            <head>
                <title>
                    <xsl:value-of select="$title"/>
                </title>
            </head>
            <body>
                <xsl:template match="/">
                    <xsl:for-each select="//ord:order[@id]">
                        <xsl:variable name="specid" select="position()"/>
                        <table>

                            <tr>
                                <th>Номер замовлення</th>
                                <th>Звідки</th>
                                <th>Куди</th>
                                <th>Зв'язок з водієм</th>
                                <th>Інформація водія</th>
                                <th>Автівка</th>
                                <th>Остаточна ціна поїздки</th>
                                <th>Ваша оцінка</th>
                            </tr>

                            <tr>

                                <td class="num">
                                    <h4>№<xsl:value-of select="position()"/>
                                    </h4>
                                </td>

                                <td class="def">
                                    <xsl:for-each select="//ord:user[@id=$specid]">
                                        <h4>З
                                            <xsl:value-of select="user:addressFrom"/>
                                        </h4>
                                    </xsl:for-each>
                                </td>

                                <td class="def">
                                    <xsl:for-each select="//ord:user[@id=$specid]">
                                        <h4>До
                                            <xsl:value-of select="user:addressTo"/>
                                        </h4>
                                    </xsl:for-each>
                                </td>

                                <td class="def">
                                    <xsl:for-each select="//ord:driver[@id=$specid]">
                                        <h4>По телефону:
                                            <br/>
                                            <xsl:value-of select="driv:phone"/>
                                        </h4>
                                    </xsl:for-each>
                                </td>

                                <td class="def">
                                    <xsl:for-each select="//ord:driver[@id=$specid]">
                                        <h4>Ім'я:
                                            <xsl:value-of select="driv:name"/>
                                            <br/>
                                            Прізвище:
                                            <xsl:value-of select="driv:surname"/>
                                            <br/>
                                            Середня оцінка:
                                            <xsl:value-of select="driv:mark"/>
                                        </h4>
                                    </xsl:for-each>
                                </td>

                                <td class="def">
                                    <xsl:for-each select="//ord:driver[@id=$specid]">
                                        <xsl:for-each select="//driv:car[@id=$specid]">
                                            <h4>
                                                <b>
                                                    <xsl:value-of select="driv:brand"/>
                                                    &#160;
                                                    <xsl:value-of select="driv:name"/>
                                                    <br/>
                                                    Номер:
                                                    <n style="color:red;"><xsl:value-of select="driv:number"/></n>
                                                    <br/>
                                                    <xsl:value-of select="driv:c_class"/>-клас
                                                </b>
                                            </h4>
                                        </xsl:for-each>
                                    </xsl:for-each>
                                </td>

                                <td class="num">
                                    <h4 style="color:green; font-family: 'Courier New', monospace; padding:5px;">
                                        <xsl:value-of select="ord:price"/> ₴
                                    </h4>
                                </td>

                                <td class="num">
                                    <xsl:variable name="mark" select="ord:mark"/>
                                    <xsl:if test="$mark &lt;5">
                                        <h4 style="color:red; font-family: 'Courier New', monospace;">
                                            <xsl:value-of select="$mark"/>
                                        </h4>
                                    </xsl:if>
                                    <xsl:if test="$mark&lt;8 and $mark&gt;4">
                                        <h4 style="color:orange; font-family: 'Courier New', monospace;">
                                            <xsl:value-of select="$mark"/>
                                        </h4>
                                    </xsl:if>
                                    <xsl:if test="$mark>7">
                                        <h4 style="color:green; font-family: 'Courier New', monospace;">
                                            <xsl:value-of select="$mark"/>
                                        </h4>
                                    </xsl:if>
                                </td>

                            </tr>

                        </table>

                    </xsl:for-each>

                </xsl:template>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>