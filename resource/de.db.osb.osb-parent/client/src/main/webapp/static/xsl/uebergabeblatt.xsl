<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	
	<!-- Seitenaufbau - - - - - - - - - - - - - - - - - - - - - - - 
							titel
							- - - 
	
	empfaengerlist		masterniederlassung		rb beteiligt fplonr
						sender
	- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	
	bbp					formular				version
	massnahmen
	strecken
	- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	
	Zugdaten/Knotenzeiten
	 -->
	
	<!-- Seiteneinstellungen -->
	<xsl:template match="/">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
			<fo:layout-master-set>
				<fo:simple-page-master master-name="simple"
					page-height="21cm" page-width="29.7cm" margin-top="1cm"
					margin-bottom="2cm" margin-left="1.5cm" margin-right="1.5cm">
					<fo:region-body margin-top="3cm" />
					<fo:region-before extent="3cm" />
					<fo:region-after extent="1.5cm" />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="simple">
				<fo:flow flow-name="xsl-region-body">
					<fo:block>
						<xsl:apply-templates select="zvfexport" />
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	
	<!-- Hauptlayout -->
	<xsl:template match="zvfexport">
		<!-- Seitentitel -->
		<xsl:apply-templates select="baumassnahmen/baumassnahme/version/titel" />
		<fo:table table-layout="fixed" width="100%" space-after="10px">
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<!-- Empfänger -->
						<xsl:apply-templates select="header/empfaengerlist" />
					</fo:table-cell>
					<fo:table-cell>
						<!-- Absender -->
						<xsl:apply-templates select="baumassnahmen/baumassnahme/masterniederlassung" />
						<xsl:apply-templates select="header/sender" />
					</fo:table-cell>
					<fo:table-cell>
						<!-- Beteiligte Regionalbereiche -->
						<fo:table table-layout="fixed" width="100%">
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<xsl:apply-templates select="baumassnahmen/baumassnahme/fplonr" mode="rb"/>
									</fo:table-cell>
									<fo:table-cell>
										<xsl:apply-templates select="baumassnahmen/baumassnahme/fplonr" mode="beteiligt"/>
									</fo:table-cell>
									<fo:table-cell>
										<xsl:apply-templates select="baumassnahmen/baumassnahme/fplonr" mode="fplonr"/>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		
		<!-- Formulardaten -->
		<fo:table table-layout="fixed" width="100%" space-after="10px"  border="1pt solid gray">
			<fo:table-column column-width="25%"/>
			<fo:table-column column-width="30%"/>
			<fo:table-column column-width="45%"/>
			<fo:table-body>
				<fo:table-row>
					<fo:table-cell>
						<xsl:apply-templates select="baumassnahmen/baumassnahme/bbpliste" />
					</fo:table-cell>
					<fo:table-cell>
						<xsl:apply-templates select="baumassnahmen/baumassnahme/version/formular" />
					</fo:table-cell>
					<fo:table-cell>
						<xsl:apply-templates select="baumassnahmen/baumassnahme/version" />
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>
		
		<!--  Massnahmen -->
		<fo:table table-layout="fixed" width="100%" space-after="10px" border="1pt solid gray">
			<fo:table-column column-width="15%"/>
			<fo:table-column column-width="85%"/>
			<fo:table-body>
				<xsl:apply-templates select="baumassnahmen/baumassnahme/streckenabschnitte" mode="massnahme"/>
			</fo:table-body>
		</fo:table>
		
		<!-- Strecken -->
		<fo:table table-layout="fixed" width="100%"  space-after="20px" border="1pt solid gray">
			<fo:table-column column-width="20%"/>
			<fo:table-column column-width="20%"/>
			<fo:table-column column-width="10%"/>
			<fo:table-column column-width="25%"/>
			<fo:table-column column-width="25%"/>
			<fo:table-body>
				<xsl:apply-templates select="baumassnahmen/baumassnahme/streckenabschnitte" mode="zeitraum"/>
			</fo:table-body>
		</fo:table>
		
		<!-- Zugdaten/Knotenzeiten -->
		<fo:table table-layout="fixed" width="100%" font-size="8pt" text-align="center" border="1pt solid gray">
			<fo:table-column column-width="7%"/>
			<fo:table-column column-width="6%"/>
			<fo:table-column column-width="5%"/>
			<fo:table-column column-width="12%"/>
			<fo:table-column column-width="12%"/>
			<fo:table-column column-width="33%"/>
			<fo:table-column column-width="5%"/>
			<fo:table-column column-width="5%"/>
			<fo:table-column column-width="5%"/>
			<fo:table-column column-width="5%"/>
			<fo:table-column column-width="5%"/>
			<fo:table-header>
				<fo:table-row>
					<fo:table-cell>
						<fo:block font-weight="bold"><xsl:text>Datum</xsl:text></fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold"><xsl:text>Gattung</xsl:text></fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold"><xsl:text>Zugnr</xsl:text></fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold"><xsl:text>Abgangsbahnhof</xsl:text></fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold"><xsl:text>Zielbahnhof</xsl:text></fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold"><xsl:text>Knotenzeiten</xsl:text></fo:block>
						<fo:table table-layout="fixed" width="100%" text-align="center">
							<fo:table-column column-width="15%"/>
							<fo:table-column column-width="15%"/>
							<fo:table-column column-width="25%"/>
							<fo:table-column column-width="25%"/>
							<fo:table-column column-width="20%"/>
							<fo:table-header>
								<fo:table-row>
									<fo:table-cell>
										<fo:block font-weight="bold"><xsl:text>Bahnhof</xsl:text></fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-weight="bold"><xsl:text>Haltart</xsl:text></fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-weight="bold"><xsl:text>An</xsl:text></fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block font-weight="bold"><xsl:text>Ab</xsl:text></fo:block>
									</fo:table-cell>	
									<fo:table-cell>
										<fo:block font-weight="bold"><xsl:text>Relativlage</xsl:text></fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-header>
							<fo:table-body>
								<fo:table-row><fo:table-cell><fo:block></fo:block></fo:table-cell></fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold"><xsl:text>Tfz</xsl:text></fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold"><xsl:text>Last</xsl:text></fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold"><xsl:text>Mbr+ Brems-stellung</xsl:text></fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold"><xsl:text>Zug-länge</xsl:text></fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold"><xsl:text>VMax</xsl:text></fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>
			<fo:table-body>
				<xsl:apply-templates select="baumassnahmen/baumassnahme/zuege" />
			</fo:table-body>
		</fo:table>
	</xsl:template>
	
	<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  -->
	<!-- Teillayouts -->
		
	<xsl:template match="titel">
		<fo:block font-size="18pt" font-weight="bold"
			color="black" text-align="center" padding-bottom="20px" padding-top="0px">
			<xsl:value-of select="." />
		</fo:block>
	</xsl:template>
		
	<xsl:template match="empfaengerlist">
		<fo:block>
			<fo:block font-weight="bold" text-decoration="underline"><xsl:text>Empfänger:</xsl:text></fo:block>
			<xsl:for-each select="empfaenger">
		    	<fo:block>
		    		<xsl:text>-&#160;</xsl:text>
		    		<xsl:value-of select="." />
		    	</fo:block>
		  	</xsl:for-each>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="masterniederlassung">
		<fo:block font-weight="bold">
			<xsl:value-of select="." />
		</fo:block>
	</xsl:template>
	
		<xsl:template match="sender">
		<fo:block font-weight="bold" padding-bottom="10px">
			<xsl:apply-templates select="abteilung" />
			<xsl:text>&#160;</xsl:text>
			<xsl:apply-templates select="kuerzel" />
		</fo:block> 
		<fo:block>
			<xsl:apply-templates select="strasse" />
		</fo:block>
		<fo:block padding-bottom="10px">
			<xsl:apply-templates select="plz" />
			<xsl:text>&#160;</xsl:text>
			<xsl:apply-templates select="ort" />		
		</fo:block>
		<fo:block>
			<xsl:text>Tel. intern: </xsl:text> 
			<xsl:apply-templates select="telefon_intern" />		
		</fo:block>
		<fo:block>
			<xsl:text>Tel. extern: </xsl:text> 
			<xsl:apply-templates select="telefon" />		
		</fo:block>
		<fo:block>
			<xsl:text>Email: </xsl:text>
			<xsl:apply-templates select="email" />		
		</fo:block>		
	</xsl:template>
	
	<xsl:template match="fplonr" mode="rb">
		<fo:block font-weight="bold">
			<xsl:text>RB</xsl:text>
		</fo:block>
		<fo:block>
			<xsl:for-each select="niederlassung">
		    	<fo:block>
		    		<xsl:choose>
			    		<xsl:when test=". = 'sued'">
							<xsl:text>Süd</xsl:text>		    				
	    				</xsl:when>
	    				<xsl:when test=". = 'nord'">
							<xsl:text>Nord</xsl:text>		    				
	    				</xsl:when>
	    				<xsl:when test=". = 'mitte'">
							<xsl:text>Mitte</xsl:text>		    				
	    				</xsl:when>
	    				<xsl:when test=". = 'suedost'">
							<xsl:text>Südost</xsl:text>		    				
	    				</xsl:when>
	    				<xsl:when test=". = 'west'">
							<xsl:text>West</xsl:text>		    				
	    				</xsl:when>
	    				<xsl:when test=". = 'suedwest'">
							<xsl:text>Südwest</xsl:text>		    				
	    				</xsl:when>
	    				<xsl:when test=". = 'ost'">
							<xsl:text>Ost</xsl:text>		    				
	    				</xsl:when>
	    			</xsl:choose>
		    	</fo:block>
		  	</xsl:for-each>
		</fo:block>
	</xsl:template>	
	
	<xsl:template match="fplonr" mode="beteiligt">
		<fo:block font-weight="bold">
			<xsl:text>beteiligt</xsl:text>
		</fo:block>
		<fo:block>
			<xsl:for-each select="niederlassung">
		    	<fo:block>
		    		<xsl:choose>
			    		<xsl:when test="@beteiligt = 1">
							<xsl:text>Ja</xsl:text>		    				
	    				</xsl:when>
			    		<xsl:otherwise>
							<xsl:text>Nein</xsl:text>		    				
	    				</xsl:otherwise>
    				</xsl:choose>
		    	</fo:block>
		  	</xsl:for-each>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="fplonr" mode="fplonr">
		<fo:block font-weight="bold">
			<xsl:text>Fplo-Nr.</xsl:text>
		</fo:block>
		<fo:block>
			<xsl:for-each select="niederlassung">
		    	<fo:block>
			    	<xsl:choose>
			    		<xsl:when test="@fplo">
			    			<xsl:value-of select="@fplo" />
			    		</xsl:when>
			    		<xsl:otherwise>
							<xsl:text>&#160;</xsl:text>	<!-- Leerzeichen -->	    				
	    				</xsl:otherwise>
			    	</xsl:choose>
		    	</fo:block>
		  	</xsl:for-each>
		</fo:block>
	</xsl:template>
	
	<xsl:template match="formular">
		<fo:block>
			<xsl:text>Formularkennung: </xsl:text>
			<xsl:value-of select="." />
		</fo:block>
	</xsl:template>
	
	<xsl:template match="version">
		<fo:block>
			<xsl:text>Version: </xsl:text>
			<xsl:value-of select="major" />
			<xsl:text>.</xsl:text>
			<xsl:value-of select="minor" />
			<xsl:text>.</xsl:text>
			<xsl:value-of select="sub" />
		</fo:block>
	</xsl:template>
	
	<xsl:template match="bbpliste">
		<fo:block>
			<xsl:text>BBP-Strecke: </xsl:text>
			<xsl:for-each select="bbp">
				<fo:block>
					<xsl:text>&#160;&#160;</xsl:text>
					<xsl:value-of select="." />
				</fo:block>
			</xsl:for-each>
		</fo:block>
	</xsl:template>
		
	<xsl:template match="streckenabschnitte" mode="massnahme">
		<xsl:for-each select="strecke">
			<fo:table-row border="1pt solid gray">
				<fo:table-cell>
					<fo:block>
						<xsl:text>VZG-Strecke(n): </xsl:text>
						<xsl:apply-templates select="vzgliste"/>
					</fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block>
						<xsl:text>Betroffener Bereich: </xsl:text>
		    			<xsl:value-of select="massnahme" />
					</fo:block>
				</fo:table-cell>
			</fo:table-row>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="vzgliste">
		<xsl:for-each select="vzg">
			<fo:block>
			<xsl:text>&#160;&#160;</xsl:text>
			<xsl:value-of select="." />
			</fo:block>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="streckenabschnitte" mode="zeitraum">
		 <xsl:for-each select="strecke">
		 	<fo:table-row border="1pt solid gray">
				<fo:table-cell>
					<fo:block>
		    			<xsl:value-of select="baubeginn" />
		    		</fo:block>
		    	</fo:table-cell>
		    	<fo:table-cell>
		    		<fo:block>
		    			<xsl:value-of select="bauende" />
		    		</fo:block>
		    	</fo:table-cell>
		    	<fo:table-cell>
		    		<fo:block>
		    			<xsl:value-of select="zeitraum_unterbrochen" />
		    		</fo:block>
		    	</fo:table-cell>
		    	<fo:table-cell>
		    		<fo:block>
		    			<xsl:value-of select="grund" />
		    		</fo:block>
		    	</fo:table-cell>
		    	<fo:table-cell>
		    		<fo:block>
		    			<xsl:value-of select="betriebsweise" />
		    		</fo:block>
		    	</fo:table-cell>
		    </fo:table-row>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="zuege">
		<xsl:for-each select="zug">
			<fo:table-row border="1pt solid gray" keep-together="always">
				<fo:table-cell>
					<fo:block>
		    			<xsl:value-of select="@verkehrstag" />
		    		</fo:block>
		    	</fo:table-cell>
		    	<fo:table-cell>
					<fo:block>
		    			<xsl:value-of select="@zugbez" />
		    		</fo:block>
		    	</fo:table-cell>
		    	<fo:table-cell>
					<fo:block>
		    			<xsl:value-of select="@zugnr" />
		    		</fo:block>
		    	</fo:table-cell>
		    	<fo:table-cell>
					<fo:block>
		    			<xsl:value-of select="regelweg/abgangsbahnhof" />
		    		</fo:block>
		    	</fo:table-cell>
		    	<fo:table-cell>
					<fo:block>
		    			<xsl:value-of select="regelweg/zielbahnhof" />
		    		</fo:block>
		    	</fo:table-cell>
		    	<fo:table-cell>
					<fo:block>
					   <xsl:apply-templates select="knotenzeiten" />
		    		</fo:block>
				</fo:table-cell>
		    	<fo:table-cell>
					<fo:block>
		    			<xsl:value-of select="zugdetails/tfz" />
		    		</fo:block>
		    	</fo:table-cell>
		    	<fo:table-cell>
					<fo:block>
		    			<xsl:value-of select="zugdetails/last" />
		    		</fo:block>
		    	</fo:table-cell>
		    	<fo:table-cell>
					<fo:block>
		    			<xsl:value-of select="zugdetails/brems" />
		    		</fo:block>
		    	</fo:table-cell>
		    	<fo:table-cell>
					<fo:block>
		    			<xsl:value-of select="zugdetails/laenge" />
		    		</fo:block>
		    	</fo:table-cell>
		    	<fo:table-cell>
					<fo:block>
		    			<xsl:value-of select="zugdetails/vmax" />
		    		</fo:block>
		    	</fo:table-cell>
		    </fo:table-row>
	    </xsl:for-each>
	</xsl:template>
	
	<xsl:template match="knotenzeiten">
		<fo:table table-layout="fixed" width="100%" text-align="center">
			<fo:table-column column-width="15%"/>
			<fo:table-column column-width="15%"/>
			<fo:table-column column-width="25%"/>
			<fo:table-column column-width="25%"/>
			<fo:table-column column-width="20%"/>
			<fo:table-body>
				<xsl:choose>
					<xsl:when test="knotenzeit">
						<xsl:for-each select="knotenzeit">
							<fo:table-row>
								<fo:table-cell>
									<fo:block>
										<xsl:value-of select="bahnhof" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block>
										<xsl:value-of select="haltart" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block>
										<xsl:value-of select="ankunft" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block>
										<xsl:value-of select="abfahrt" />
									</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<fo:block>
										<xsl:value-of select="relativlage" />
									</fo:block>
								</fo:table-cell>
							</fo:table-row>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<fo:table-row>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block></fo:block>
							</fo:table-cell>
						</fo:table-row>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-body>
		</fo:table>
	</xsl:template>
</xsl:stylesheet>