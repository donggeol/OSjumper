using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using NAudio.Wave;
using NAudio.Utils;


namespace Micro_input
{
    public class JhjProvider : IWaveProvider
    {
        private CircularBuffer circularBuffer;
        //private CircularBuffer circularBuffer = new CircularBuffer(10000000);
        private WaveFormat waveFormat;

        /// <summary>
        /// Creates a new buffered WaveProvider
        /// </summary>
        /// <param name="waveFormat">WaveFormat</param>
        public JhjProvider(WaveFormat waveFormat)
        {
            this.waveFormat = waveFormat;
            this.BufferLength = waveFormat.AverageBytesPerSecond * 5;
        }

        /// <summary>
        /// Buffer length in bytes
        /// </summary>
        public int BufferLength { get; set; }

        /// <summary>
        /// Buffer duration
        /// </summary>
        public TimeSpan BufferDuration
        {
            get
            {
                return TimeSpan.FromSeconds((double)BufferLength / WaveFormat.AverageBytesPerSecond);
            }
            set
            {
                BufferLength = (int)(value.TotalSeconds * WaveFormat.AverageBytesPerSecond);
            }
        }

        /// <summary>
        /// If true, when the buffer is full, start throwing away data
        /// if false, AddSamples will throw an exception when buffer is full
        /// </summary>
        public bool DiscardOnBufferOverflow { get; set; }

        /// <summary>
        /// The number of buffered bytes
        /// </summary>
        public int BufferedBytes
        {
            get { if (circularBuffer == null) return 0; return circularBuffer.Count; }
        }

        /// <summary>
        /// Buffered Duration
        /// </summary>
        public TimeSpan BufferedDuration
        {
            get { return TimeSpan.FromSeconds((double)BufferedBytes / WaveFormat.AverageBytesPerSecond); }
        }

        /// <summary>
        /// Gets the WaveFormat
        /// </summary>
        public WaveFormat WaveFormat
        {
            get { return waveFormat; }
        }

        /// <summary>
        /// Adds samples. Takes a copy of buffer, so that buffer can be reused if necessary
        /// </summary>
        public void AddSamples(byte[] buffer, int offset, int count)
        {
            // create buffer here to allow user to customise buffer length
            if (this.circularBuffer == null)
            {
                this.circularBuffer = new CircularBuffer(this.BufferLength);
            }

            this.circularBuffer.Write(buffer, offset, count);
            int written = this.circularBuffer.Read(buffer, offset, count);
            //int written = this.circularBuffer.Write(buffer, offset, count);

            if (written < count && !DiscardOnBufferOverflow)
            {
                throw new InvalidOperationException("Buffer full");
            }
        }

        /// <summary>
        /// Reads from this WaveProvider
        /// Will always return count bytes, since we will zero-fill the buffer if not enough available
        /// </summary>
        public int Read(byte[] buffer, int offset, int count)
        {
            int read = 0;
            if (this.circularBuffer != null) // not yet created
            {
                read = this.circularBuffer.Read(buffer, offset, count);
            }
            if (read < count)
            {
                // zero the end of the buffer
                Array.Clear(buffer, offset + read, count - read);
            }
            return count;
        }
    }
}
